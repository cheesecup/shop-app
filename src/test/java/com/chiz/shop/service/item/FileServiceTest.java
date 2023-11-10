package com.chiz.shop.service.item;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class FileServiceTest {

    @Autowired FileService fileService;

    private String itemImgLocation = "C:/Project_Folder/toy-project/project-shomall-mng/Shopmall_Mng_Back/item-img";

    @Test
    @DisplayName("파일 업로드 테스트")
    void FILE_UPLOAD_TEST() throws Exception {
        String oriImgName = "이미지_원본_이름.jpg";
        byte[] fileData = {1,2,3,4};

        String savedFileName = fileService.uploadFile(itemImgLocation, oriImgName, fileData);

        System.out.println("저장된 파일 이름 : " + savedFileName);
    }
}