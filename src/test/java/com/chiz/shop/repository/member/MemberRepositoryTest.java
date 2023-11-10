package com.chiz.shop.repository.member;

import com.chiz.shop.domain.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    @PersistenceContext EntityManager em;

    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "chiz@email.com", password = "12341234", roles = "USER")
    void AUDITING_TEST() {
        Member member = Member.of("chiz", null, null, "Seoul", null);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(IllegalArgumentException::new);

        System.out.println("regTime : " + findMember.getRegTime());
        System.out.println("createdBy : " + findMember.getCreatedBy());
        System.out.println("updateTime : " + findMember.getUpdateTime());
        System.out.println("modifiedBy : " + findMember.getModifiedBy());
    }
}