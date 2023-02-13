package com.todo.app.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class GeneratorUtil {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyz";

    public String generateUID(int length) {

        StringBuilder userId = new StringBuilder(length);

        for (int i= 0 ; i < length; i++){
            userId.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return userId.toString();

    }

}
