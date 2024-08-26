package luckyvicky.petharmony.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import luckyvicky.petharmony.dto.WordDTO;
import luckyvicky.petharmony.entity.Word;
import luckyvicky.petharmony.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class WordServiceImpl implements WordService {
    private final WordRepository wordRepository;

    /**
     * 모든 단어 조회
     *
     * @return 모든 단어
     */
    @Override
    public List<WordDTO> getAllWord() {
        List<Word> words = wordRepository.findAll();

        // Word 객체를 WordDTO로 변환하여 리스트로 반환
        return words.stream()
                .map(word -> new WordDTO(
                        word.getWordId(),
                        word.getWordSelect()
                ))
                .collect(Collectors.toList());
    }
}
