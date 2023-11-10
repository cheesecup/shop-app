package com.chiz.shop.controller;

import com.chiz.shop.dto.item.ItemFormDto;
import com.chiz.shop.service.item.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(@Autowired ItemService itemService) {
        this.itemService = itemService;
    }

    // 상품 등록 컨트롤러
    @PostMapping("/admin/items/new")
    public String createItem(@Valid ItemFormDto dto,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()) {
            return "item/itemCreateForm";
        }

        if (itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수적으로 입력해주세요.");
            return "item/itemCreateForm";
        }

        try {
            Long itemId = itemService.saveItem(dto, itemImgFileList);
            return "redirect:/items/info/" + itemId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 중 오류가 발생했습니다.");
            return "item/itemCreateForm";
        }

    }

    // 상품 정보 수정 컨트롤러
    @PostMapping("/admin/items/update/{itemId}")
    public String updateItem(@PathVariable Long itemId,
                             @Valid ItemFormDto dto,
                             BindingResult bindingResult,
                             Model model,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()) {
            return "item/itemUpdateForm";
        }

        if (itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수적으로 입력해주세요.");
            return "item/itemUpdateForm";
        }

        try {
            itemService.updateItem(itemId, dto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
            return "item/itemUpdateForm";
        }

        return "redirect:/admin/items/" + itemId;
    }
}
