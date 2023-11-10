package com.chiz.shop.dto.request.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class CreateMemberDto {

    @NotBlank(message = "이름은 필수 입력입니다.")
    private String memberName;

    @NotBlank(message = "이메일은 필수 입력입니다.")
    @Email(message = "이메일 형식이 잘못 되었습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String pwd;

    @NotBlank(message = "주소는 필수 입력입니다.")
    private String address;

    public CreateMemberDto() {
    }

    @Builder
    public CreateMemberDto(String memberName, String email, String pwd, String address) {
        this.memberName = memberName;
        this.email = email;
        this.pwd = pwd;
        this.address = address;
    }

}
