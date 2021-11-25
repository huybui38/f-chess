package com.example.fchess.web.security.oauth2.user;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo{

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return this.attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return this.attributes.get("email").toString();
    }

    @Override
    public String getAvatar() {
        return this.attributes.get("picture").toString();
    }
}
