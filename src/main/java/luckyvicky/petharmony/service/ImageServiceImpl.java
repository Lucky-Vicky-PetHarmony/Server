package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Image;
import luckyvicky.petharmony.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;

    /**
     * 스토리지에 이미지를 저장, db에 이미지 저장
     *
     * 주어진 파일 리스트를 순회하며 각각의 파일을 S3 스토리지에 업로드하고,
     * 해당 파일의 메타데이터(이름, UUID, URL 등)를 포함한 `Image` 엔티티를 생성하여 반환함.
     *
     * @param board 이미지와 연관된 게시판 엔티티
     * @param files 업로드할 이미지 파일 리스트
     * @return
     * @throws IOException 파일 업로드 중 발생할 수 있는 예외
     */
    @Override
    public void addImage(Board board, List<MultipartFile> files) throws IOException {
        List<Image> images = new ArrayList<>();

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString();  // UUID 생성
            String imageUrl = s3Service.saveFile(file, uuid);  // S3에 파일 업로드 후 URL 얻기

            Image image = Image.builder()
                    .board(board)
                    .imageName(file.getOriginalFilename())
                    .imageUuid(uuid)
                    .imageUrl(imageUrl)
                    .build();

            images.add(image);
        }
        imageRepository.saveAll(images);
    }

    /**
     * DB, 스토리지에서 이미지파일 삭제
     *
     * @param ids 삭제할 이미지 파일의 id들
     * @throws IOException 파일 삭제 시 발생할 수 있는 예외
     */
    @Override
    public void deleteImage(List<Long> ids) throws IOException {
        for (Long id : ids) {
            Image image = imageRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지않은 imageId"));

            // 스토리지에서 이미지 삭제
            s3Service.deleteFile(image.getImageUuid());
            // DB에서 image 삭제
            imageRepository.deleteById(id);
        }
    }

    /**
     * boardId에 해당하는 imageId들 찾기
     *
     * @param boardId
     * @return imageId를 리스트 형태로 반환
     */
    @Override
    public List<Long> findImageIdsByBoardId(Long boardId) {
        return imageRepository.findImageIdsByBoardId(boardId);
    }
}
