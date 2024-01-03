package org.example.jwt;

import org.example.jwt.Service.JWT.JWTService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtApplicationTests {
    private final JWTService jwtService;

    @Autowired
    public JwtApplicationTests(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Test
    void contextLoads() {

        String jwt = jwtService.createJWT(1L);

        System.out.println("jwt = " + jwt);

    }

}
