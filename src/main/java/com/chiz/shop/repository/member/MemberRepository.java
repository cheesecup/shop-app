package com.chiz.shop.repository.member;

import com.chiz.shop.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일로 회원 검색
    Member findByEmail(String email);
}
