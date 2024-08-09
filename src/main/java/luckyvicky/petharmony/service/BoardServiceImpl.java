package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardPostDTO;
import luckyvicky.petharmony.dto.board.BoardUpdateDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Image;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.ImageRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private final S3Service s3Service;

    /**
     * 스토리지에 이미지를 저장
     *
     * 주어진 파일 리스트를 순회하며 각각의 파일을 S3 스토리지에 업로드하고,
     * 해당 파일의 메타데이터(이름, UUID, URL 등)를 포함한 `Image` 엔티티를 생성하여 반환함.
     *
     * @param board 이미지와 연관된 게시판 엔티티
     * @param files 업로드할 이미지 파일 리스트
     * @return 저장된 이미지의 `Image` 엔티티 리스트
     * @throws IOException 파일 업로드 중 발생할 수 있는 예외
     */
    private List<Image> savedImages(Board board, List<MultipartFile> files) throws IOException {
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

        return images;
    }

    /**
     * DB, 스토리지에서 이미지파일 삭제
     *
     * @param deleteImageIds 삭제할 이미지파일의 id들
     * @throws IOException 파일 삭제 시 발생할 수 있는 예외
     */
    private void deleteImages(List<Long> deleteImageIds) throws IOException {
        for (Long id : deleteImageIds) {
            Image image = imageRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지않은 imageId"));

            // 스토리지에서 이미지 삭제
            s3Service.deleteFile(image.getImageUuid());
            // DB에서 image 삭제
            imageRepository.deleteById(id);
        }
    }


    /**
     * 게시물 작성
     *
     * @param boardPostDTO 제목, 내용, 카테고리, 작성자 ID, 첨부파일을 포함하는 DTO
     * @return BoardId 게시판 번호를 반환
     * @throws IOException 파일 업로드 시 발생할 수 있는 예외
     */
    @Override
    public Long boardPost(BoardPostDTO boardPostDTO) throws IOException {
        User user = userRepository.findById(boardPostDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        List<Image> imageList = new ArrayList<>(); // 이미지 배열을 저장

        Board board = Board.builder()
                .boardTitle(boardPostDTO.getTitle())
                .boardContent(boardPostDTO.getContent())
                .category(boardPostDTO.getCategory())
                .user(user)
                .build();
        boardRepository.save(board);

        // 이미지 파일이 있을때만 실행
        if (boardPostDTO.getImages() != null && !boardPostDTO.getImages().isEmpty()) {
            imageList = savedImages(board, boardPostDTO.getImages());
        }


        imageRepository.saveAll(imageList);

        return board.getBoardId();
    }


    /**
     * 게시물 수정
     *
     * @param boardUpdateDTO 제목, 내용, 카테고리, 작성자 ID, 삭제한 첨부파일, 추가한 첨부파일을 포함하는 DTO
     * @return BoardId 게시판 번호를 반환
     * @throws IOException 파일 업로드 시 발생할 수 있는 예외
     */
    @Override
    public Long boardUpdate(BoardUpdateDTO boardUpdateDTO) throws IOException {
        Board board = boardRepository.findById(boardUpdateDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));
        List<Image> imageList = new ArrayList<>(); //추가된 이미지 배열을 저장

        Board updateBoard = board.toBuilder()
                .boardTitle(boardUpdateDTO.getTitle())
                .boardContent(boardUpdateDTO.getContent())
                .category(boardUpdateDTO.getCategory())
                .build();

        // 삭제한 이미지가 있을 경우
        if (boardUpdateDTO.getDeleteImages() != null && !boardUpdateDTO.getDeleteImages().isEmpty()) {
            deleteImages(boardUpdateDTO.getDeleteImages());
        }

        // 추가한 이미지 파일이 있을때만 실행
        if (boardUpdateDTO.getUpdateImages() != null && !boardUpdateDTO.getUpdateImages().isEmpty()) {
            imageList = savedImages(board, boardUpdateDTO.getUpdateImages());
        }
        boardRepository.save(updateBoard);
        imageRepository.saveAll(imageList);

        return board.getBoardId();
    }
}
