package com.chiz.shop.service.member;

import com.chiz.shop.dto.member.MemberFormDto;
import com.chiz.shop.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired PasswordEncoder passwordEncoder;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    @Test
    @DisplayName("회원 정보 저장 테스트")
    void TEST_MEMBER_SAVE() {
        MemberFormDto dto = new MemberFormDto("user1", "user1@email.com", "12341234", "서울특별시");
        Long memberId = memberService.saveMember(dto, passwordEncoder);

        assertThat("user1").isEqualTo(memberRepository.findById(memberId).get().getMemberName());
    }

    @Test
    @DisplayName("중복 회원 검증 테스트")
    void TEST_VALIDATE_MEMBER() {
        // given
        MemberFormDto dto1 = new MemberFormDto("user1", "user1@email.com", "12341234", "서울특별시");
        MemberFormDto dto2 = new MemberFormDto("user2", "user1@email.com", "12341234", "여수시");
        
        // when
        memberService.saveMember(dto1, passwordEncoder);
        Throwable e = catchThrowable(() -> memberService.saveMember(dto2, passwordEncoder));

        // then
        assertThat(e).isInstanceOf(IllegalStateException.class).hasMessage("이미 가입된 회원입니다.");
    }
}