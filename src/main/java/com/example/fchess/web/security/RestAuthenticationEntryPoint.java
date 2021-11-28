package com.example.fchess.web.security;

import com.example.fchess.web.payload.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class RestAuthenticationEntryPoint  implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        logger.error("Responding with unauthorized error. Message - {}", e.getMessage());
        ApiResponse apiResponse = new ApiResponse(e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        apiResponse.addError(e.getMessage());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(out, apiResponse.toMap());
        out.flush();
//        resolver.resolveException(httpServletRequest, httpServletResponse, null, e);
//        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
//                e.getLocalizedMessage());
    }
}
