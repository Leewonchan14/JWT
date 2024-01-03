package org.example.jwt.Model.DTO;

import lombok.Builder;
import lombok.Getter;

public class MemberRequestDTO {

    @Getter
    @Builder
    public static class MemberCreateRequestDTO {
        private String username;
        private String password;
    }


    @Getter
    @Builder
    public static class MemberLoginRequestDTO {
        private String username;
        private String password;
    }
}
