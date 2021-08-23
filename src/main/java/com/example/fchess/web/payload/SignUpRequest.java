package com.example.fchess.web.payload;

import com.example.fchess.web.validator.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@FieldMatch(first = "password", second = "rePassword")
public class SignUpRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String rePassword;
    @NotBlank
    private String nickname;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
