package com.YouRL.aop;


import com.YouRL.annotations.RateLimit;
import com.YouRL.enums.LimitMethod;
import com.YouRL.enums.LimitType;
import com.YouRL.ratelimit.RateLimiter;
import com.YouRL.ratelimit.RateLimiterExecutor;
import com.YouRL.ratelimit.payload.RateVariables;
import com.YouRL.util.ClientIPUtils;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Configuration
public class RateLimiterProcessor {
    private static final Logger log = LoggerFactory.getLogger(RateLimiterProcessor.class);
    private final RateLimiterExecutor rateLimiterExecutor;

    @Autowired
    public RateLimiterProcessor(RateLimiterExecutor rateLimiterExecutor) {
        this.rateLimiterExecutor = rateLimiterExecutor;
    }

    @Around("execution(public * *(..)) && @annotation(com.YouRL.annotations.RateLimit)")
    public Object processor(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (method == null) {
            return null;
        } else {
            RateLimit rateLimitAnnotation = method.getAnnotation(RateLimit.class);
            LimitType limitType = rateLimitAnnotation.limitType();
            String key = getLimiterKey(request, method, rateLimitAnnotation, limitType);
            key = StringUtils.join(rateLimitAnnotation.prefix(), key);

            double permitsPerSecond = rateLimitAnnotation.permitsPerSecond();
            int period = rateLimitAnnotation.period();
            int permits = rateLimitAnnotation.permits();
            LimitMethod limitMethod = rateLimitAnnotation.limitMethod();

            RateVariables rateVariables = new RateVariables();
            rateVariables.setKey(key);
            rateVariables.setPermits(permits);
            rateVariables.setPeriod(period);
            rateVariables.setPermitsPerSecond(permitsPerSecond);
            RateLimiter rateLimiter = switch (limitMethod) {
                case PERMITS_BUCKET -> rateLimiterExecutor.getPermitsBucketRateLimiter();
            };

            if (rateLimiter.isRateLimited(key, rateVariables)) {
                log.info("Access to {} from {} is rate limited", method.getName(), key);
                sendFallback();
                return null;
            }
        }

            return joinPoint.proceed();
    }

    private String getLimiterKey(HttpServletRequest request, Method method, RateLimit rateLimitAnnotation, LimitType limitType) {
        String key = StringUtils.upperCase(
                method.getDeclaringClass().getSimpleName() + ":" + method.getName()
        );
        return switch (limitType) {
            case IP -> key + "_" + LimitType.IP + ":" + ClientIPUtils.getClientIpAddress(request);
        };
    }

    private void sendFallback() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        log.info("TOO MANY REQUESTS");
    }
}