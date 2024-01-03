package org.example.jwt.Model.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberResponseDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MemberPreviewDTO {
        private Long id;
        private String username;
        private String password;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MemberCreateResponseDTO {
        private Long memberId;
        private LocalDateTime createdAt;
        private String token;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MemberLoginResponseDTO {
        private Long memberId;
        private String token;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class MemberAutoLoginResponseDTO {
        private Long memberId;
        private Boolean isSuccess;
    }
}
