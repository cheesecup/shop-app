package com.chiz.shop.dto.response.member;

import com.chiz.shop.constant.Role;
import com.chiz.shop.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MemberInfoResponse {

    private Long id;
    private String email;
    private String memberName;
    private String address;
    private Role role;

    private LocalDateTime regTime;

    public MemberInfoResponse() {
    }

    public MemberInfoResponse(Long id, String email, String memberName, String address, Role role, LocalDateTime regTime) {
        this.id = id;
        this.email = email;
        this.memberName = memberName;
        this.address = address;
        this.role = role;
        this.regTime = regTime;
    }

    public static MemberInfoResponse toResponse(Member member) {
        return new MemberInfoResponse(member.getId(),
                member.getEmail(),
                member.getMemberName(),
                member.getAddress(),
                member.getRole(),
                member.getRegTime());
    }
}
