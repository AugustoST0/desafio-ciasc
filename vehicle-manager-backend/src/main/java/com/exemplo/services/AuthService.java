package com.exemplo.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.exemplo.exception.exceptions.InvalidCredentialsException;
import com.exemplo.exception.exceptions.InvalidRefreshTokenException;
import com.exemplo.model.user.LoginRequestDTO;
import com.exemplo.model.user.TokenResponseDTO;
import com.exemplo.model.user.User;
import com.exemplo.security.JWTTokenProvider;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class AuthService {

    @Inject
    UserService userService;

    @Inject
    JWTTokenProvider jwtTokenProvider;

    @Inject
    JWTParser jwtParser;

    @Transactional
    public TokenResponseDTO authenticateUser(LoginRequestDTO dto) {
        User user = userService.getUserByEmail(dto.email());

        BCrypt.Result result = BCrypt.verifyer().verify(dto.password().toCharArray(), user.getPassword());

        if (!result.verified) {
            throw new InvalidCredentialsException("Senha incorreta");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        return new TokenResponseDTO(accessToken, refreshToken);
    }

    public TokenResponseDTO refreshTokens(String refreshToken) {
        try {
            JsonWebToken jwt = jwtParser.parse(refreshToken);
            String email = jwt.getSubject();

            var user = userService.getUserByEmail(email);

            String newAccessToken = jwtTokenProvider.generateAccessToken(email);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

            return new TokenResponseDTO(newAccessToken, newRefreshToken);

        } catch (ParseException e) {
            throw new InvalidRefreshTokenException("Refresh token inválido ou malformado");
        }
    }
}
