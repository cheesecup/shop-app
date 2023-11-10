package com.chiz.shop.service.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    // 로컬에 파일 저장
    // 랜덤이름.확장자 형식으로 저장된 파일 이름 반환
    public String uploadFile(String uploadPath,
                             String oriImgName,
                             byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();
        String extension = oriImgName.substring(oriImgName.lastIndexOf(".")); // 이미지 파일 확장자(extension) 추출

        String savedFileName = uuid.toString() + extension; // 랜덤이름 + 확장자 형식으로 이미지 파일 저장
        String uploadFileFullURL = uploadPath + "/" + savedFileName;

        FileOutputStream fos = new FileOutputStream(uploadFileFullURL);
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    // 로컬에 저장된 파일 삭제
    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
