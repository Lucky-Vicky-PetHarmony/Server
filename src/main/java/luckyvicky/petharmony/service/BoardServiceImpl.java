package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.board.BoardPostDTO;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.board.Board;
import luckyvicky.petharmony.entity.board.Image;
import luckyvicky.petharmony.repository.BoardRepository;
import luckyvicky.petharmony.repository.ImageRepository;
import luckyvicky.petharmony.repository.UserRepository;
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
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private final S3Service s3Service;

    /**
     * @param boardPostDTO 제목, 내용, 카테고리, 작성자 ID, 첨부파일을 포함하는 DTO
     * @return BoardId 게시판 번호를 반환
     * @throws IOException 파일 업로드 시 발생할 수 있는 예외
     */
    @Override
    public Long boardPost(BoardPostDTO boardPostDTO) throws IOException {
        User user = userRepository.findById(boardPostDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        List<Image> images = new ArrayList<>();

        Board board = Board.builder()
                .boardTitle(boardPostDTO.getTitle())
                .boardContent(boardPostDTO.getContent())
                .category(boardPostDTO.getCategory())
                .user(user)
                .build();

        // 이미지파일이 있을때만 실행
        if (boardPostDTO.getImages() != null && !boardPostDTO.getImages().isEmpty()) {
            for (MultipartFile file : boardPostDTO.getImages()) {
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
        }

        boardRepository.save(board);
        imageRepository.saveAll(images);

        return board.getBoardId();
    }
}
