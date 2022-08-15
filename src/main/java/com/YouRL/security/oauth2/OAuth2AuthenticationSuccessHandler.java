package com.YouRL.security.oauth2;

import com.YouRL.config.WebSecurityJWTProperties;
import com.YouRL.security.jwt.JwtUtils;
import com.YouRL.util.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.YouRL.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final int cookieExpireSeconds = 1800;
    private final List<String> AUTHORIZED_URI = Arrays.asList(
            "http://127.0.0.1:3000",
            "http://127.0.0.1:3000/login",
            "http://localhost:3000/home",
            "http://localhost:3000/oauth2/redirect"
    );

    @Autowired
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    private WebSecurityJWTProperties webSecurityJWTProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, IOException {

        log.info("oauth2 handler");

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.info("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        String token = JwtUtils.generateJwtToken(
                authentication,
                webSecurityJWTProperties.getTokenExpiration(),
                webSecurityJWTProperties.getSecretKey());

        log.info("cookie: " + URLEncoder.encode(token, StandardCharsets.UTF_8));
        response.addHeader(HttpHeaders.SET_COOKIE, CookieUtils.createCookieString(
                webSecurityJWTProperties.getAccessTokenCookieName(),
                token,
                cookieExpireSeconds
        ));
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new RuntimeException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        log.info(getDefaultTargetUrl());
        return redirectUri.orElse(getDefaultTargetUrl());
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        for(String uriString : AUTHORIZED_URI) {
            URI authorizedUri = URI.create(uriString);
            if(authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) &&
                    authorizedUri.getPort() == clientRedirectUri.getPort()) {
                return true;
            }
        }
        return false;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
