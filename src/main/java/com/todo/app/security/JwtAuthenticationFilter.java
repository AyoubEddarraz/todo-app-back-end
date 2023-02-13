package com.todo.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.todo.app.dto.requests.LoginRequest;
import com.todo.app.dto.responses.ErrorResponse;
import com.todo.app.dto.responses.TokenResponse;
import com.todo.app.entities.UserEntity;
import com.todo.app.exceptions.errorsMessages.UserErrorsMessages;
import com.todo.app.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {

            LoginRequest creeds = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creeds.getEmail(), creeds.getPassword(), new ArrayList<>())
            );

        }catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String email = ((User) authResult.getPrincipal()).getUsername();

        UserEntity user = userRepository.findByEmail(email);

        // check if user Enable
        // TODO :: CHECK IF USER ENABLED AND TRY TO HANDLER IF USER NOT ALREADY REGISTERED AND NEED TO CONFIRM EMAIL LATTER
        if (!user.getAccountEnabled()) {

            ErrorResponse errorResponse = new ErrorResponse(new Date(), UserErrorsMessages.USER_IS_NOT_ENABLED.getErrorMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());

            String errorResponseString = new Gson().toJson(errorResponse);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());
            response.getWriter().write(errorResponseString);

        }

        if (user.getAccountLocked()) {

            ErrorResponse errorResponse = new ErrorResponse(new Date(), UserErrorsMessages.USER_IS_LOCKED.getErrorMessage(), HttpStatus.LOCKED.value());

            String errorResponseString = new Gson().toJson(errorResponse);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.LOCKED.value());
            response.getWriter().write(errorResponseString);

        }

        if (user != null && user.getAccountEnabled() && !user.getAccountLocked()) {


            Map<String, Object> tokenClaims = new HashMap<>();
            tokenClaims.put("UID" , user.getUserId());
            tokenClaims.put("roles", new ArrayList<>());
            tokenClaims.put("authorities", new ArrayList<>());
            tokenClaims.put("email", user.getEmail());

            String accessToken = Jwts.builder()
                    .setSubject(email)
                    .setClaims(tokenClaims)
                    .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_ACCESS_TOKEN))
                    .signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                    .compact();

            String refreshToken = Jwts.builder()
                    .setSubject(email)
                    .setClaims(tokenClaims)
                    .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_REFRESH_TOKEN))
                    .signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                    .compact();

            TokenResponse tokenResponse =
                    new TokenResponse(
                            accessToken,
                            refreshToken,
                            user.getUserId(),
                            new ArrayList<>(),
                            new ArrayList<>()
                    );

            String tokenResponseString = new Gson().toJson(tokenResponse);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(tokenResponseString);

        }


    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        ErrorResponse errorResponse = new ErrorResponse(new Date(), "email or password incorrect", HttpStatus.UNAUTHORIZED.value());
        String errorResponseString = new Gson().toJson(errorResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(errorResponseString);

    }

}
