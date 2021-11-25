package com.example.fchess.web.payload;

import com.example.fchess.web.validator.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
@FieldMatch(first = "password", second = "rePassword", message = "SIGNUP_REQUEST.REPASSWORD.NOT_CORRECT")
public class SignUpRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank(message = "SIGNUP_REQUEST.PASSWORD.NOT_BLANK")
    private String password;
    @NotBlank(message = "SIGNUP_REQUEST.REPASSWORD.NOT_BLANK")
    private String rePassword;
    @NotBlank(message = "SIGNUP_REQUEST.NICKNAME.NOT_BLANK")
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
