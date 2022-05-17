package com.hineks.epos.config;

public class SecurityConstants {

    public static final String SECRET = "Emakina";
    public static final long EXPIRATION_TIME = 423_000_000; // 5 gün
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/developers/sign-up";
}
