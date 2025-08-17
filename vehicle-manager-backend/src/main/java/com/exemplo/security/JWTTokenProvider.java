package com.exemplo.security;

import com.exemplo.model.user.User;
import com.exemplo.services.UserService;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JWTTokenProvider {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @Inject
    UserService userService;

    public String generateAccessToken(String email) {
        User user = userService.getUserByEmail(email);
        Set<String> groups = new HashSet<>();
        groups.add("USER");

        if(user.isAdmin()) {
            groups.add("ADMIN");
        }

        return Jwt.issuer(issuer)
                .subject(email)
                .claim("userId", user.getId())
                .claim("userName", user.getName())
                .groups(groups)
                .expiresIn(Duration.ofMinutes(60))
                .sign();
    }

    public String generateRefreshToken(String email) {
        return Jwt.issuer(issuer)
                .subject(email)
                .expiresIn(Duration.ofDays(7))
                .sign();
    }
}
