package com.todo.app.security;

import com.todo.app.entities.UserEntity;
import com.todo.app.exceptions.errorsExceptions.UserException;
import com.todo.app.exceptions.errorsMessages.UserErrorsMessages;
import com.todo.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class PrincipalUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity user = userRepository.findByEmail(email);

        // Check if user Exist
        if (user == null) throw new UserException(UserErrorsMessages.USER_NOT_FOUND.getErrorMessage());

        UserPrincipal userPrincipal = new UserPrincipal(user);

        return new User(userPrincipal.getUsername(), userPrincipal.getPassword(), new ArrayList<>());

    }

}
