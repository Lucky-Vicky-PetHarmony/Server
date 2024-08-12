package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.WordClassificationDTO;
import luckyvicky.petharmony.repository.PetInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CachedPetInfoWordService {

    /**
     * PetInfoRepository를 통해 데이터베이스와 상호작용
     * 특정 조건에 맞는 PetInfo 데이터를 조회하고, 해당 데이터를 WordClassificationDTO로 변환하여 반환
     */
    @Autowired
    private PetInfoRepository petInfoRepository;

    /**
     * 특정 연령과 성별에 따라 WordClassificationDTO 리스트를 조회하고, 이를 캐시에 저장
     * 캐싱된 데이터를 통해 동일한 조건으로 반복되는 데이터베이스 조회를 방지하고 성능을 최적화
     *
     * @param age    조회하려는 대상의 연령 조건.
     * @param sexCd  조회하려는 대상의 성별 조건.
     * @return       해당 조건에 맞는 WordClassificationDTO 객체 리스트.
     *
     * 주요 동작:
     * 1. 이 메서드는 Spring의 캐시 추상화를 사용하여 결과를 캐시에 저장
     * 2. 동일한 조건으로 이 메서드가 호출되면, 데이터베이스 조회 대신 캐시된 데이터를 반환
     * 3. 캐시는 'wordClassifications'라는 이름으로 식별되며, 이는 캐시 설정에 따라 메모리 또는 다른 저장소에 유지
     */
    @Cacheable("wordClassifications")
    public List<WordClassificationDTO> getCachedWordClassifications(String age, String sexCd) {
        // 페이징을 통해 최대 100개의 WordClassificationDTO 객체를 반환
        // 이 예시에서는 첫 번째 페이지(0번 인덱스)만 가져오며, 이는 캐싱의 효율성을 높이기 위함
        return petInfoRepository.findWordClassificationsByCriteria(age, sexCd, PageRequest.of(0, 100)).getContent();
    }
}
