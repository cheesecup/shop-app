package com.chiz.shop.controller;

import com.chiz.shop.dto.member.MemberFormDto;
import com.chiz.shop.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public MemberController(@Autowired MemberService memberService,
                            @Autowired PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원정보 저장
    @PostMapping("/members/new")
    public String createMember(@Valid MemberFormDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("createMemberDto", new MemberFormDto());
            return "member/memberCreateForm";
        }

        try {
            memberService.saveMember(dto, passwordEncoder);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberCreateForm";
        }

        return "redirect:/";
    }

}
