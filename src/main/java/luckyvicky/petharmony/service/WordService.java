package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.WordDTO;

import java.util.List;

public interface WordService {
    //모든 단어 조회
    List<WordDTO> getAllWord();
}
