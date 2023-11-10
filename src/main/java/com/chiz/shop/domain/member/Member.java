package com.chiz.shop.domain.member;

import com.chiz.shop.constant.Role;
import com.chiz.shop.domain.BaseEntity;
import com.chiz.shop.dto.member.MemberFormDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@ToString
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id = null;

    private String memberName; // 이름

    @Column(unique = true)
    private String email; // 이메일

    private String pwd; // 비밀번호

    private String address; // 주소

    @Enumerated(EnumType.STRING)
    private Role role; // 권한

    protected Member() {
    }

    private Member(String memberName, String email, String pwd, String address, Role role) {
        this.memberName = memberName;
        this.email = email;
        this.pwd = pwd;
        this.address = address;
        this.role = role;
    }

    // 생성자 생성
    public static Member of(String memberName, String email, String pwd, String address, Role role) {
        return new Member(memberName, email, pwd, address, role);
    }

    public static Member createMember(MemberFormDto dto, PasswordEncoder passwordEncoder) {
        String pwd = passwordEncoder.encode(dto.getPwd());
        return Member.of(dto.getName(),
                dto.getEmail(),
                pwd,
                dto.getAddress(),
                Role.ADMIN);
    }
}
