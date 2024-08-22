package luckyvicky.petharmony.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    //파일업로드
    String saveFile(MultipartFile multipartFile, String uuid) throws IOException;

    //파일삭제
    void deleteFile(String fileName);

}
