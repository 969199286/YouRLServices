package com.YouRL.security.oauth2;

import com.YouRL.util.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 1800;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        try {
            //  Block of code to try
            OAuth2AuthorizationRequest c =  CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                    .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                    .orElse(null);
        }
        catch(Exception e) {
            //  Block of code to handle errors
            log.info(e.getMessage());
        }

        OAuth2AuthorizationRequest c =  CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
        log.info(c.getRedirectUri());
        return c;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        log.info("saved");
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(
                    request,
                    response,
                    OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
            );

            CookieUtils.deleteCookie(
                    request,
                    response,
                    REDIRECT_URI_PARAM_COOKIE_NAME
            );
            return;
        }

        CookieUtils.addCookie(
                response,
                OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtils.serialize(authorizationRequest), cookieExpireSeconds
        );

        String redirectUriAfterLogin = request.getParameter(
                REDIRECT_URI_PARAM_COOKIE_NAME
        );

        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(
                    response,
                    REDIRECT_URI_PARAM_COOKIE_NAME,
                    redirectUriAfterLogin,
                    cookieExpireSeconds
            );
        }

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        log.info("remove auth request old version");
        return this.loadAuthorizationRequest(request);
    }


    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(
                request,
                response,
                OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME
        );

        CookieUtils.deleteCookie(
                request,
                response,
                REDIRECT_URI_PARAM_COOKIE_NAME
        );
    }
}