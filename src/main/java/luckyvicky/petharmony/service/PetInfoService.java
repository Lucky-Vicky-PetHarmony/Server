package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.PetInfoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetInfoService {

    private final PetInfoRepository petInfoRepository;

    /**
     * PetInfoService 생성자
     *
     * @param petInfoRepository PetInfo 엔티티와 상호작용하는 리포지토리
     */
    public PetInfoService(PetInfoRepository petInfoRepository) {
        this.petInfoRepository = petInfoRepository;
    }

    /**
     * 사용자가 선택한 단어에 따라 상위 12개의 PetInfo를 반환하는 메서드
     *
     * @param user 현재 로그인된 사용자
     * @return     사용자가 선택한 단어와 매칭된 상위 12개의 PetInfo 리스트
     */
    public List<PetInfo> getTop12PetInfosByUserWord(User user) {
        return petInfoRepository.findTop12ByUserWord(user, PageRequest.of(0, 12));
    }
}