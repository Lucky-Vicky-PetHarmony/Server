package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.entity.UserWord;
import luckyvicky.petharmony.repository.PetInfoRepository;
import luckyvicky.petharmony.repository.UserWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetInfoService {

    private final PetInfoRepository petInfoRepository;
    private final UserWordRepository userWordRepository;

    @Autowired
    public PetInfoService(PetInfoRepository petInfoRepository, UserWordRepository userWordRepository) {
        this.petInfoRepository = petInfoRepository;
        this.userWordRepository = userWordRepository;
    }

    /**
     * 특정 사용자가 선택한 word_id와 매칭되는 상위 12개의 PetInfo를 반환하는 메서드
     *
     * @param userId 현재 로그인된 사용자의 ID
     * @return 상위 12개의 PetInfo 리스트
     */
    public List<PetInfo> getTop12PetInfosByUserWord(Long userId) {
        // userId에 해당하는 모든 word_id 리스트를 가져옴
        List<Long> wordIds = userWordRepository.findWordIdsByUserId(userId);

        if (wordIds.isEmpty()) {
            return List.of(); // wordIds가 비어있으면 빈 리스트 반환
        }

        // word_id 리스트와 매칭되는 PetInfo 리스트 반환
        return petInfoRepository.findTop12ByWordIds(wordIds, PageRequest.of(0, 12));
    }
}