package com.todo.app.constants;

public class MailConstants {

    public static final String MAIN_MAIL = "ayoub.developer8@gmail.com";
    public static final String MAIL_PASSWORD = "fcfnqtsnuyhoetdo";
    public static final long CONFIRMATION_MAIL_EXPIRATION_TIME = 600000; // 60000 -> 1 minute --- 600000 -> 10 Minutes

    public static final String CONFIRMATION_EMAIL = "please activate your account";
    public static final String RESET_PASSWORD = "Reset your password";
    public static final String DELETE_ACCOUNT = "We're sad to see you go";
    public static final String ACCOUNT_DELETED = "account deleted";
    public static final String PASSWORD_CHANGED = "password changed";

    public static final String CONFIRMATION_MAIL_HTML = "EmailVerification.html";
    public static final String RESET_PASSWORD_HTML = "ResetPassword.html";
    public static final String DELETE_ACCOUNT_HTML = "deleteAccount.html";
    public static final String ACCOUNT_DELETED_HTML = "accountDeleted.html";
    public static final String PASSWORD_CHANGED_HTML = "passwordChanged.html";

    public static final String CONFIRMATION_EMAIL_COVER_URL = "https://firebasestorage.googleapis.com/v0/b/devmarocblog.appspot.com/o/app_images%2F83962-mail-sent.gif?alt=media&token=f90a8405-a6b7-452c-a332-cb1ddc798440";
    public static final String RESET_PASSWORD_COVER_URL = "https://firebasestorage.googleapis.com/v0/b/devmarocblog.appspot.com/o/app_images%2F92615-forgot-password-password-reset.gif?alt=media&token=1236cd7d-a80f-4cec-a0f4-f5f6fb03bc17";
    public static final String DELETE_ACCOUNT_COVER_URL = "https://firebasestorage.googleapis.com/v0/b/devmarocblog.appspot.com/o/app_images%2F11865-sad-emoji.gif?alt=media&token=41c0acf4-4a80-400c-9e0d-61025eed5163";

    public static final String CONFIRM_EMAIL_BASE_URL = "http://localhost:4200/account/confirm";
    public static final String RESET_PASSWORD_BASE_URL = "http://localhost:4200/account/resetPassword";
    public static final String DELETE_ACCOUNT_BASE_URL = "http://localhost:4200/account/deleteAccount";

}
