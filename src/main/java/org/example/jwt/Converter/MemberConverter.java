package org.example.jwt.Converter;

import org.example.jwt.Model.DAO.Member;
import org.example.jwt.Model.DTO.MemberRequestDTO;
import org.example.jwt.Model.DTO.MemberResponseDTO;

public class MemberConverter {
    public static Member toMember(MemberRequestDTO.MemberCreateRequestDTO request) {
        return Member.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }

    public static MemberResponseDTO.MemberPreviewDTO toMemberPreviewDTO(Member member) {
        return MemberResponseDTO.MemberPreviewDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public static MemberResponseDTO.MemberCreateResponseDTO toMemberCreateResponseDTO(Member member) {
        return MemberResponseDTO.MemberCreateResponseDTO.builder()
                .memberId(member.getId())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public static MemberResponseDTO.MemberLoginResponseDTO toMemberLoginResponseDTO(Member member, String token) {
        return MemberResponseDTO.MemberLoginResponseDTO.builder()
                .memberId(member.getId())
                .token(token)
                .build();
    }

    public static MemberResponseDTO.MemberAutoLoginResponseDTO toMemberAutoLoginResponseDTO(Member member) {
        return MemberResponseDTO.MemberAutoLoginResponseDTO.builder()
                .memberId(member.getId())
                .isSuccess(true)
                .build();
    }
}
