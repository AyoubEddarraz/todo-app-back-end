package com.todo.app.security;

import com.google.gson.Gson;
import com.todo.app.dto.responses.ErrorResponse;
import com.todo.app.entities.UserEntity;
import com.todo.app.exceptions.errorsMessages.JwtErrorsMessages;
import com.todo.app.exceptions.errorsMessages.UserErrorsMessages;
import com.todo.app.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(JwtProperties.HEADER);

        if(header != null){

            try {
                String token = header.replace(JwtProperties.PREFIX , "");

                Claims claims = Jwts.parser()
                        .setSigningKey(JwtProperties.SECRET)
                        .parseClaimsJws(token)
                        .getBody();

                String user = ((String) claims.get("email"));
                String userId = ((String) claims.get("UID"));

                UserEntity userEntity = userRepository.findByEmailAndUserId(user, userId);

                if (userEntity.getAccountEnabled() && !userEntity.getAccountLocked()) {

                    List<String> rolesList = (List<String>) claims.get("roles");
                    List<String> authoritiesList = (List<String>) claims.get("authorities");

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for (String role:rolesList) authorities.add(new SimpleGrantedAuthority(role));
                    for (String authority:authoritiesList) authorities.add(new SimpleGrantedAuthority(authority));

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user , null , authorities);

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    chain.doFilter(request, response);

                }else if (!userEntity.getAccountEnabled()) {

                    ErrorResponse errorResponse = new ErrorResponse(new Date(), UserErrorsMessages.USER_IS_NOT_ENABLED.getErrorMessage(), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());
                    String errorResponseString = new Gson().toJson(errorResponse);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());
                    response.getWriter().write(errorResponseString);

                }else if (userEntity.getAccountLocked()) {

                    ErrorResponse errorResponse = new ErrorResponse(new Date(), UserErrorsMessages.USER_IS_LOCKED.getErrorMessage(), HttpStatus.LOCKED.value());
                    String errorResponseString = new Gson().toJson(errorResponse);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpStatus.LOCKED.value());
                    response.getWriter().write(errorResponseString);

                }


            }catch (Exception e){
                ErrorResponse errorResponse = new ErrorResponse(new Date(), JwtErrorsMessages.JWT_EXPIRED_OR_NOT_VALID.getErrorMessage(), HttpStatus.UNAUTHORIZED.value());
                String tokenErrorResponse = new Gson().toJson(errorResponse);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(tokenErrorResponse);
            }

        }else{
            chain.doFilter(request, response);
            return;
        }


    }


}
