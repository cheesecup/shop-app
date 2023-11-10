package com.chiz.shop.controller;

import com.chiz.shop.dto.member.MemberFormDto;
import com.chiz.shop.dto.request.member.CreateMemberDto;
import com.chiz.shop.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    public Long createMember(String email, String pwd) {
        MemberFormDto dto = new MemberFormDto("홍길동", email, pwd, "서울특별시");

        return memberService.saveMember(dto, passwordEncoder);
    }

    @Test
    @DisplayName("시큐리티를 이용한 로그인 성공 테스트")
    void TEST_LOGIN_SUCCESS_USING_SECURITY() throws Exception {
        String email = "test@test.com";
        String pwd = "12341234";
        Long memberId = this.createMember(email, pwd);

        mockMvc.perform(formLogin().userParameter("email")
                        .passwordParam("pwd")
                        .loginProcessingUrl("/members/login")
                        .user(email)
                        .password(pwd))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("시큐리티를 이용한 로그인 실패 테스트")
    void TEST_LOGIN_FAIL_USING_SECURITY() throws Exception {
        String email = "test@test.com";
        String pwd = "12341234";
        this.createMember(email, pwd);

        mockMvc.perform(formLogin().userParameter("email")
                .passwordParam("pwd")
                .loginProcessingUrl("/members/login")
                .user(email)
                .password("errorPwd"))
                .andExpect(unauthenticated());

    }
}