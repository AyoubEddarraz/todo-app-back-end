package com.todo.app.security;

public class JwtProperties {

    public static final String SECRET = "QnRUYjsWPl0A7M4Dn7tV3Q-gbxx2tRdBCSVyiZ7UkG_fspLd-8EU16wNZRoDpiwR3co7etxkV5ukud42DW-rx0woXB_XSqRCGfAtI9oYLThaQ-HSQowlo234jiF0N8BzR3lVgFnrpn";
    public static final int EXPIRATION_TIME_ACCESS_TOKEN = 864000000; // 10days
    //public static final int EXPIRATION_TIME_ACCESS_TOKEN = 600000; // 10 Minutes
    public static final long EXPIRATION_TIME_REFRESH_TOKEN = 31557600000l; // 365days
    public static final String SING_UP_URL = "/api/v1/account/register";
    public static final String REFRESH_TOKEN = "/api/v1/account/refresh";
    public static final String PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

}
