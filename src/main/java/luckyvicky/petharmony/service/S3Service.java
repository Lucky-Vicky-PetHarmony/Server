package luckyvicky.petharmony.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일을 AWS S3에 업로드하고, 업로드된 파일의 공개 URL을 반환
     *
     * @param multipartFile 업로드할 파일
     * @param uuid 파일 이름(고유 식별자)
     * @return 업로드된 파일의 공개 URL
     * @throws IOException 파일 업로드 중 입출력 오류가 발생할 경우
     */
    public String saveFile(MultipartFile multipartFile, String uuid) throws IOException, IOException {
        String fileName = uuid; //uuid와 파일의 확장자를 조합하여 고유한 파일 이름을 생성

        //S3에 업로드할 파일의 메타데이터를 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize()); //파일크기
        metadata.setContentType(multipartFile.getContentType()); //MIME 타입

        //putObject(버킷이름, 파일이름, 업로드할 파일, metadata)
        amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    // 파일 삭제 메소드
    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucket, fileName);
    }
}
