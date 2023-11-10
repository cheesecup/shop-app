package com.chiz.shop.controller;

import com.chiz.shop.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class PageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("상품 등록 페이지 관리자 권한 테스트")
    @WithMockUser(username = "admin", password = "1234", roles = "ADMIN")
    void ITEM_REGISTER_PAGE_PERMISSION_TEST() throws Exception {
        mockMvc.perform(get("/admin/items/new"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("상품 등록 페이지 일반 권한 테스트 ")
    @WithMockUser(username = "user", password = "1234", roles = "USER")
    public void itemFormNotAdminTest() throws Exception{
        mockMvc.perform(get("/admin/item/new"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}