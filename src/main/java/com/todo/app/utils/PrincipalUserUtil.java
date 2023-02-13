package com.todo.app.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public class PrincipalUserUtil {

    // Role of this Function Get username of the current authenticated user To handle operations of app
    public String getPrincipalUserName() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        return principal.getName();
    }

}
