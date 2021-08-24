package com.example.fchess.web.controller;

import com.example.fchess.web.exception.ResourceNotFoundException;
import com.example.fchess.web.model.User;
import com.example.fchess.web.repository.UserRepository;
import com.example.fchess.web.security.CurrentUser;
import com.example.fchess.web.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public User getMe(@CurrentUser UserPrincipal userPrincipal){
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
