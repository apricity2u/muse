package com.example.muse.global.common.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class JwtConfigTest {
    @Autowired
    private SecretKey jwtSecretKey;
    @Test
    void secretKey가_32바이트_이상이다() {
        int keyLength = jwtSecretKey.getEncoded().length;
        assertTrue(keyLength >= 32, "Secret key length must be at least 32 bytes.");
    }

}