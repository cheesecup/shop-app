package com.chiz.shop.controller;

import com.chiz.shop.domain.item.Item;
import com.chiz.shop.dto.cart.CartDetailDto;
import com.chiz.shop.dto.item.*;
import com.chiz.shop.dto.member.MemberFormDto;
import com.chiz.shop.dto.order.OrderHistDto;
import com.chiz.shop.service.cart.CartService;
import com.chiz.shop.service.item.ItemService;
import com.chiz.shop.service.member.MemberService;
import com.chiz.shop.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

// 페이지 이동 컨트롤러
@Slf4j
@Controller
public class PageController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderService orderService;
    private final CartService cartService;

    public PageController(@Autowired ItemService itemService,
                          @Autowired MemberService memberService,
                          @Autowired OrderService orderService,
                          @Autowired CartService cartService) {
        this.itemService = itemService;
        this.memberService = memberService;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    // 메인 페이지
    @GetMapping("/")
    public String home(ItemSearchDto itemSearchDto,
                       Optional<Integer> page,
                       Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

    // 회원 목록 페이지
    @GetMapping("/admin/members")
    public String memberListForm(Model model) {
        List<MemberFormDto> members = memberService.memberList();
        model.addAttribute("members", members);

        return "member/memberList";
    }

    // 회원 정보 단건 조회
    @GetMapping("/admin/members/{memberId}")
    public String getMemberInfo(@PathVariable Long memberId, Model model) {
        MemberFormDto member = memberService.findMemberInfo(memberId);
        model.addAttribute("member", member);

        return "member/memberManageForm";
    }

    // 회원가입 페이지
    @GetMapping("/members/new")
    public String createMemberForm(Model model) {
        model.addAttribute("createMemberDto", new MemberFormDto());
        return "member/memberCreateForm";
    }

    // 로그인 페이지
    @GetMapping("/members/login")
    public String loginForm() {
        return "member/loginForm";
    }

    // 로그인 에러 페이지
    @GetMapping("/members/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/loginForm";
    }

    // 상품 목록 조회 페이지
    @GetMapping("/admin/items")
    public String itemListForm(Model model) {
        List<ItemDto> items = itemService.itemList();
        model.addAttribute("items", items);

        return "item/itemListForm";
    }

    // 상품 등록 페이지
    @GetMapping("/admin/items/new")
    public String createItemForm(Model model) {
        model.addAttribute("createItemDto", new ItemFormDto());
        model.addAttribute("itemImgDto", new ItemImgDto());
        return "item/itemCreateForm";
    }

    // 상품 수정 페이지
    @GetMapping("/admin/items/update/{itemId}")
    public String itemUpdateForm(@PathVariable Long itemId, Model model) {
        ItemFormDto item = itemService.getSingleItem(itemId);

        model.addAttribute("itemFormDto", item);

        return "item/itemUpdateForm";
    }

    // 상품 정보 단건 조회 컨트롤러
    @GetMapping("/items/info/{itemId}")
    public String itemInfo(@PathVariable Long itemId, Model model) {
        ItemFormDto singleItem = itemService.getSingleItem(itemId);

        model.addAttribute("itemFormDto", singleItem);

        return "item/itemInfoForm";
    }

    @GetMapping("/item/{itemId}")
    public String itemDtl(@PathVariable Long itemId, Model model) {
        ItemFormDto itemFormDto = itemService.getSingleItem(itemId);

        model.addAttribute("item", itemFormDto);

        return "item/itemDtl";
    }

    // 상품 조검 검색 결과 페이지
    @GetMapping(value = {"/items", "/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable Optional<Integer> page,
                             Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "item/itemManageForm";
    }

    // 구매이력 조회 페이지
    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable Optional<Integer> page,
                            Principal principal,
                            Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

        Page<OrderHistDto> orderHistDtoList = orderService.getOrder(principal.getName(), pageable);

        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    // 장바구니 조회 페이지
    @GetMapping("/cart")
    public String orderHist(Principal principal, Model model) {
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName());
        model.addAttribute("cartItems", cartDetailDtoList);

        return "cart/cartList";
    }
}
