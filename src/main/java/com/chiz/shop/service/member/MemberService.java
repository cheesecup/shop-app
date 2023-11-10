package com.chiz.shop.service.member;

import com.chiz.shop.domain.member.Member;
import com.chiz.shop.dto.member.MemberFormDto;
import com.chiz.shop.repository.member.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 로그인
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPwd())
                .roles(member.getRole().toString())
                .build();
    }

    // 회원정보 저장
    public Long saveMember(MemberFormDto dto, PasswordEncoder passwordEncoder) {
        Member member = memberRepository.findByEmail(dto.getEmail());
        if (member != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }

        return memberRepository.save(Member.createMember(dto, passwordEncoder)).getId();
    }
    
    // 회원 리스트 조회
    public List<MemberFormDto> memberList() {
        return memberRepository.findAll().stream()
                .map(member -> MemberFormDto.of(member))
                .collect(Collectors.toList());
    }

    // 회원정보 단건 조회
    public MemberFormDto findMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        return MemberFormDto.of(member);
    }
}
