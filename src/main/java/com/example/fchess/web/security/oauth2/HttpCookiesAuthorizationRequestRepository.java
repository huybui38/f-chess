package com.example.fchess.web.security.oauth2;

import com.example.fchess.web.utils.CookieUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class HttpCookiesAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 180;


    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest httpServletRequest) {
        return CookieUtils.getCookie(httpServletRequest, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest oAuth2AuthorizationRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (oAuth2AuthorizationRequest == null) {
            CookieUtils.deleteCookie(httpServletRequest, httpServletResponse, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(httpServletRequest, httpServletResponse, REDIRECT_URI_PARAM_COOKIE_NAME);
            return;
        }

        CookieUtils.addCookie(httpServletResponse, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(oAuth2AuthorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = httpServletRequest.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(httpServletResponse, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest httpServletRequest) {
        return this.loadAuthorizationRequest(httpServletRequest);
    }
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
//    @Override
//    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
//        return AuthorizationRequestRepository.super.removeAuthorizationRequest(request, response);
//    }
}
