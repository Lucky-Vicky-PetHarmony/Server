package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.PetLikeRequestDTO;
import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.PetLikeRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetLikeService {

    private final PetLikeRepository petLikeRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetLikeService(PetLikeRepository petLikeRepository, UserRepository userRepository) {
        this.petLikeRepository = petLikeRepository;
        this.userRepository = userRepository;
    }

    /**
     * 사용자가 반려동물에 좋아요를 표시하는 메서드
     *
     * @param requestDTO 사용자의 ID와 반려동물의 desertionNo를 포함하는 요청 데이터
     * @return 저장된 PetLike 엔티티
     */
    public PetLike savePetLike(PetLikeRequestDTO requestDTO) {
        // User를 Optional에서 꺼내서 확인하고 예외처리
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + requestDTO.getUserId()));

        // PetLike 엔티티를 생성하고 저장
        PetLike petLike = PetLike.builder()
                .user(user)
                .desertionNo(requestDTO.getDesertionNo())
                .build();
        return petLikeRepository.save(petLike);
    }

    /**
     * 특정 사용자가 좋아요를 표시한 반려동물 목록을 조회하는 메서드입
     *
     * @param userId 사용자의 ID
     * @return 사용자가 좋아요를 표시한 PetLike 목록
     */
    public List<PetLike> getPetLikesByUser(Long userId) {
        // 사용자 ID로 PetLike 목록을 조회
        return petLikeRepository.findByUser_UserId(userId);
    }
}
