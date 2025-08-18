package com.exemplo.model.user;

public record UserUpdateResponseDTO(User user, String accessToken, String refreshToken) {
}
