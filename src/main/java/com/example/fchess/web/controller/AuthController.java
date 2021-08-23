package com.example.fchess.web.controller;

//import com.example.fchess.web.exception.BadRequestException;
import com.example.fchess.web.exception.BadRequestException;
import com.example.fchess.web.model.User;
import com.example.fchess.web.payload.LoginRequest;
import com.example.fchess.web.payload.SignUpRequest;
import com.example.fchess.web.repository.UserRepository;
import com.example.fchess.web.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/test")
    public ResponseEntity<?> login() {
    return ResponseEntity.ok("oke");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest){
        if (userRepository.existsByEmail(signUpRequest.getEmail())){
            throw new BadRequestException("Email address already in use");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setNickname(signUpRequest.getNickname());
        user.setAvatarImg("DEFAULT");
        userRepository.save(user);

        return ResponseEntity.ok("ok");
    }
}
