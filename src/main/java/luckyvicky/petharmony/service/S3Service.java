package luckyvicky.petharmony.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Service {
    //파일업로드
    String saveFile(MultipartFile multipartFile, String uuid) throws IOException;

    //파일삭제
    void deleteFile(String fileName);

}
