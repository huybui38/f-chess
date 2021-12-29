package com.example.fchess.web.config;

import com.example.fchess.web.exception.RestAuthenticationFailureHandler;
import com.example.fchess.web.security.CustomUserDetailsService;
import com.example.fchess.web.security.JwtAuthenticationFilter;
import com.example.fchess.web.security.RestAuthenticationEntryPoint;
import com.example.fchess.web.security.oauth2.CustomOAuth2UserService;
import com.example.fchess.web.security.oauth2.HttpCookiesAuthorizationRequestRepository;
import com.example.fchess.web.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.fchess.web.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private RestAuthenticationFailureHandler restAuthenticationFailureHandler;
    @Autowired
    private HttpCookiesAuthorizationRequestRepository httpCookiesAuthorizationRequestRepository;
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(encoder());
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .formLogin()
//                    .loginProcessingUrl("/auth/login")
                    .failureHandler(restAuthenticationFailureHandler)
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                    .and()
                .authorizeRequests()
                    .antMatchers("/", "/error")
                        .permitAll()
                    .antMatchers("/auth/**", "/oauth2/**")
                        .permitAll()
                .antMatchers("/index.html", "/login.html", "/callback.html")
                    .permitAll()
                .antMatchers("/css/**", "/js/**", "/img/**")
                    .permitAll()
                .antMatchers("/user/**")
                        .authenticated()
                    .and()
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(httpCookiesAuthorizationRequestRepository)
                        .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/authorize/callback/*")
                        .and()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
