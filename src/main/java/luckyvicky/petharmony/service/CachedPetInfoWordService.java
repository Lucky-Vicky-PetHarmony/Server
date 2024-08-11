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

    @Autowired
    private PetInfoRepository petInfoRepository;

    @Cacheable("wordClassifications")
    public List<WordClassificationDTO> getCachedWordClassifications(String age, String sexCd) {
        return petInfoRepository.findWordClassificationsByCriteria(age, sexCd, PageRequest.of(0, 100)).getContent();
    }
}
