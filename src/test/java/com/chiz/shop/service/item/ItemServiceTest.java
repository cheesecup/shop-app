package com.chiz.shop.service.item;

import com.chiz.shop.constant.ItemSellStatus;
import com.chiz.shop.domain.item.Item;
import com.chiz.shop.domain.item.ItemImg;
import com.chiz.shop.dto.item.ItemFormDto;
import com.chiz.shop.repository.item.ItemImgRepository;
import com.chiz.shop.repository.item.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {

    @Autowired ItemRepository itemRepository;
    @Autowired ItemService itemService;
    @Autowired ItemImgRepository itemImgRepository;

    // MOCK 파일 생성 메서드
    List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i=0; i<5; i++) {
            String path = "C:/Project_Folder/toy-project/project-shomall-mng/Shopmall_Mng_Back/item-img";
            String imageName = "image" + i + ".jpg";

            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});

            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "chiz", password = "12341234", roles = "ADMIN")
    void saveItem() throws Exception {
        // Given
        ItemFormDto dto = new ItemFormDto("테스트 상품", 35000, "테스트 상품 상세내용", 100, ItemSellStatus.SELL);
        List<MultipartFile> multipartFileList = this.createMultipartFiles();

        // When
        Long itemId = itemService.saveItem(dto, multipartFileList);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(IllegalArgumentException::new);
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        // Then
        assertThat(dto.getItemNm()).isEqualTo(item.getItemNm());
        assertThat(itemImgList.get(0).getOriImgName())
                .isEqualTo(multipartFileList.get(0).getOriginalFilename());
        assertThat(5).isEqualTo(itemImgList.size());
    }

}