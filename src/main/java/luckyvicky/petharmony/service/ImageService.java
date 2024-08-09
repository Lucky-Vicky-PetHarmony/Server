package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.board.Board;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    //이미지 추가
    void addImage(Board board, List<MultipartFile> files) throws IOException;

    //이미지 삭제
    void deleteImage(List<Long> ids) throws IOException;
}
