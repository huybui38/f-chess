package com.example.fchess.web.security.oauth2.user;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo{

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getAvatar() {
        return null;
    }
}
