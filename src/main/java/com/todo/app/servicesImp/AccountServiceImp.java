package com.todo.app.servicesImp;

import com.todo.app.constants.MailConstants;
import com.todo.app.dto.requests.RegisterRequest;
import com.todo.app.dto.responses.TokenResponse;
import com.todo.app.entities.ConfirmationTokenEntity;
import com.todo.app.entities.UserEntity;
import com.todo.app.exceptions.errorsExceptions.UserException;
import com.todo.app.exceptions.errorsMessages.JwtErrorsMessages;
import com.todo.app.exceptions.errorsMessages.UserErrorsMessages;
import com.todo.app.repositories.ConfirmationTokenRepository;
import com.todo.app.repositories.UserRepository;
import com.todo.app.security.JwtProperties;
import com.todo.app.services.AccountService;
import com.todo.app.services.MailService;
import com.todo.app.utils.GeneratorUtil;
import com.todo.app.utils.JwtUtil;
import com.todo.app.utils.PrincipalUserUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {


    private final UserRepository userRepository;

    private final GeneratorUtil generatorUtil;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final JwtUtil jwtUtil = new JwtUtil();

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final PrincipalUserUtil principal = new PrincipalUserUtil();

    @Override
    public UserEntity getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) throw new UserException(UserErrorsMessages.USER_NOT_FOUND.getErrorMessage());
        return user;
    }

    @Override
    public Object register(RegisterRequest userRegisterRequest) throws MessagingException {

        UserEntity userCheck = userRepository.findByEmail(userRegisterRequest.getEmail());

        // Check if User Exsiste
        if (userCheck != null) throw new UserException(UserErrorsMessages.USER_ALREADY_EXSISTE.getErrorMessage());

        // Manipulate User Data
        UserEntity user = new UserEntity();
        user.setName(userRegisterRequest.getName());
        user.setEmail(userRegisterRequest.getEmail());
        user.setPassword(userRegisterRequest.getPassword());
        user.setUserId(generatorUtil.generateUID(32));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Unable & Blocked Account Properties
        user.setAccountEnabled(false);
        user.setAccountLocked(false);

        userRepository.save(user);

        // Send mail confirmation email
        confirmAccountMailing(user);

        return "l'utilisateur est enregistré, merci de confirmer votre mail.";
    }

    @Override
    public TokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String header = request.getHeader(JwtProperties.HEADER);

        if (header != null || header.startsWith(JwtProperties.PREFIX)) {

            try {

                String refreshToken = header.replace(JwtProperties.PREFIX, "");

                Claims claims = Jwts.parser()
                        .setSigningKey(JwtProperties.SECRET)
                        .parseClaimsJws(refreshToken)
                        .getBody();

                String email = ((String) claims.get("email"));

                UserEntity user = getUserByEmail(email);

                Map<String, Object> tokenClaims = new HashMap<>();
                tokenClaims.put("email", user.getEmail());
                tokenClaims.put("UID" , user.getUserId());
                tokenClaims.put("roles", new ArrayList<>());
                tokenClaims.put("authorities", new ArrayList<>());

                String newAccessToken = Jwts.builder()
                        .setSubject(email)
                        .setClaims(tokenClaims)
                        .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_ACCESS_TOKEN))
                        .signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                        .compact();

                String newRefreshToken = Jwts.builder()
                        .setSubject(email)
                        .setClaims(tokenClaims)
                        .setExpiration(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME_REFRESH_TOKEN))
                        .signWith(SignatureAlgorithm.HS512, JwtProperties.SECRET)
                        .compact();

                TokenResponse tokenResponse = new TokenResponse(
                        newAccessToken,
                        newRefreshToken,
                        user.getUserId(),
                        new ArrayList<>(),
                        new ArrayList<>()
                );

                System.out.println("hhhhh this is the new generated access token : " + tokenResponse.getAccessToken());

                return tokenResponse;

            }catch (Exception e){
                throw new JwtException(JwtErrorsMessages.JWT_EXPIRED_OR_NOT_VALID.getErrorMessage());
            }

        }else {
            throw new JwtException(JwtErrorsMessages.JWT_MISSING.getErrorMessage());
        }

    }

    @Override
    public Object confirmAccountMailing(UserEntity user) throws MessagingException {

        Map<String, Object> tokenClaims = new HashMap<>();
        tokenClaims.put("email", user.getEmail());

        String verificationToken = jwtUtil.generateJwt(user.getEmail(), tokenClaims, MailConstants.CONFIRMATION_MAIL_EXPIRATION_TIME);
        String confirmation_url = MailConstants.CONFIRM_EMAIL_BASE_URL + "/" + verificationToken;

        Context context = new Context();
        context.setVariable("userLastName", user.getName());
        context.setVariable("urlConfirmation", confirmation_url);
        context.setVariable("confirmation_email_cover_url", MailConstants.CONFIRMATION_EMAIL_COVER_URL);
        context.setVariable("token", verificationToken);

        mailService.sendHtmlMail(user.getEmail(), MailConstants.CONFIRMATION_EMAIL,MailConstants.CONFIRMATION_MAIL_HTML, context);

        Claims claims = Jwts.parser()
                .setSigningKey(JwtProperties.SECRET)
                .parseClaimsJws(verificationToken)
                .getBody();

        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity();
        confirmationToken.setToken(verificationToken);
        confirmationToken.setUser(user);
        confirmationToken.setExpiredAt(claims.getExpiration());
        confirmationToken.setConfirmed(false);

        confirmationTokenRepository.save(confirmationToken);

        return "email send with success";

    }

    @Override
    public Object sendConfirmationMailAfterRegistration(String email) throws MessagingException {

        UserEntity user = userRepository.findByEmail(email);

        if (user == null) throw new UserException(UserErrorsMessages.USER_NOT_FOUND.getErrorMessage());

        if (user.getAccountEnabled()) {
            return "user already confirmed.";
        }

        confirmAccountMailing(user);

        return null;
    }

    @Override
    public Object confirmAccount(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(JwtProperties.SECRET)
                .parseClaimsJws(token)
                .getBody();

        String email = ((String) claims.get("email"));

        UserEntity user = userRepository.findByEmail(email);

        if (user == null) throw new UserException(UserErrorsMessages.USER_NOT_FOUND.getErrorMessage());

        user.setAccountEnabled(true);

        userRepository.save(user);

        confirmationTokenRepository.deleteByToken(token);

        return "user confirmed with success, now you can login to your account.";
    }


}
