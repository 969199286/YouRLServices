package com.YouRL.ratelimit;

import com.YouRL.ratelimit.payload.RateVariables;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RateLimiterExecutor {
    @Getter
    private final PermitsBucketRateLimiter permitsBucketRateLimiter;

    @Autowired
    public RateLimiterExecutor(PermitsBucketRateLimiter permitsBucketRateLimiter) {
        this.permitsBucketRateLimiter = permitsBucketRateLimiter;
    }

    public static boolean isRateLimited(RateLimiter rateLimiter, int period, int permits) {
        boolean tryAcquire = rateLimiter.tryAcquire(permits, (long) period, TimeUnit.SECONDS);
        return !tryAcquire;
    }
}
