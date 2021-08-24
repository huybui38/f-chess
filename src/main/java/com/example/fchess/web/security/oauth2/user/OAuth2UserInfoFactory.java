package com.example.fchess.web.security.oauth2.user;

import com.example.fchess.web.exception.OAuth2AuthenticationProcessingException;
import com.example.fchess.web.model.eAuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationID, Map<String, Object> attributes)  {
        if (registrationID.equalsIgnoreCase(eAuthProvider.google.toString())){
            return new GoogleOAuth2UserInfo(attributes);
        }
        throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationID + " is not supported yet.");
    }
}
