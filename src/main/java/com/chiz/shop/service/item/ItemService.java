package com.chiz.shop.service.item;

import com.chiz.shop.domain.item.Item;
import com.chiz.shop.domain.item.ItemImg;
import com.chiz.shop.dto.item.*;
import com.chiz.shop.repository.item.ItemImgRepository;
import com.chiz.shop.repository.item.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;

    public ItemService(@Autowired ItemRepository itemRepository,
                       @Autowired ItemImgRepository itemImgRepository,
                       @Autowired ItemImgService itemImgService) {
        this.itemRepository = itemRepository;
        this.itemImgRepository = itemImgRepository;
        this.itemImgService = itemImgService;
    }

    // 상품 목록 조회
    @Transactional(readOnly = true)
    public List<ItemDto> itemList() {
        return itemRepository.findAll().stream()
                .map(ItemDto::of)
                .collect(Collectors.toList());
    }

    // 상품 단건 조회
    @Transactional(readOnly = true)
    public ItemFormDto getSingleItem(Long itemId) {
        List<ItemImgDto> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId).stream()
                .map(ItemImgDto::of)
                .collect(Collectors.toList());

        Item item = itemRepository.findById(itemId)
                .orElseThrow(IllegalArgumentException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgList);

        return itemFormDto;
    }

    // 페이징 정보를 담은 상품 조건 검색
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    // 페이징 정보를 담은 상품 검색 결과 메인화면에 반환
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }

    // 상품 등록 로직
    @Transactional
    public Long saveItem(ItemFormDto dto, List<MultipartFile> itemImgFileList) throws Exception {
        // 상품 등록
        Item item = itemRepository.save(Item.createItem(dto));

        // 이미지 등록
        for (int i=0; i<itemImgFileList.size(); i++) {
            String repImgYn = "N";
            if (i == 0) {
                repImgYn = "Y";
            }
            ItemImg itemImg = ItemImg.saveItemImg(repImgYn, item);

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    // 상품 정보, 이미지 수정
    @Transactional
    public Long updateItem(Long itemId, ItemFormDto dto, List<MultipartFile> itemImgFileList) throws Exception {
        // 상품 정보 수정
        Item item = itemRepository.findById(itemId)
                .orElseThrow(IllegalArgumentException::new);
        item.updateItem(dto);

        List<Long> itemImgIds = dto.getItemImgIds();

        // 상품 이미지 수정
        for (int i=0; i<itemImgFileList.size(); i++) {
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }

        return item.getId();
    }
}
