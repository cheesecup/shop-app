package com.chiz.shop.dto.member;

import com.chiz.shop.constant.Role;
import com.chiz.shop.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class MemberFormDto {

    private Long id;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 0, max = 16, message = "비밀번호는 8자 이상, 16자 이하입니다.")
    private String pwd;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    private Role role;

    private LocalDateTime regTime;

    public MemberFormDto() {}

    public MemberFormDto(Long id, String name, String email, String pwd, String address, Role role, LocalDateTime regTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pwd = pwd;
        this.address = address;
        this.role = role;
        this.regTime = regTime;
    }

    public MemberFormDto(Long id, String name, String email, String pwd, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pwd = pwd;
        this.address = address;
    }

    public MemberFormDto(String name, String email, String pwd, String address) {
        this.name = name;
        this.email = email;
        this.pwd = pwd;
        this.address = address;
    }

    public static MemberFormDto of(Member member) {
        return new MemberFormDto(member.getId(),
                member.getMemberName(),
                member.getEmail(),
                member.getPwd(),
                member.getAddress(),
                member.getRole(),
                member.getRegTime());
    }
}
