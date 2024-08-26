package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.PetLikeRequestDTO;
import luckyvicky.petharmony.dto.PetLikeResponseDTO;
import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.PetLikeRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetLikeServiceImpl implements PetLikeService {

    private final PetLikeRepository petLikeRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetLikeServiceImpl(PetLikeRepository petLikeRepository, UserRepository userRepository) {
        this.petLikeRepository = petLikeRepository;
        this.userRepository = userRepository;
    }

    /**
     * 사용자가 반려동물을 좋아요 처리하는 메서드
     *
     * @param requestDTO 사용자의 ID와 반려동물의 desertionNo를 포함하는 요청 데이터
     * @return PetLikeResponseDTO 좋아요 처리된 반려동물의 정보가 담긴 DTO
     */
    @Override
    public PetLikeResponseDTO savePetLike(PetLikeRequestDTO requestDTO) {
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        PetLike petLike = new PetLike();
        petLike.assignUserAndDesertionNo(user, requestDTO.getDesertionNo());  // 엔티티 메서드로 설정

        PetLike savedPetLike = petLikeRepository.save(petLike);

        return new PetLikeResponseDTO(
                savedPetLike.getLikeId(),
                savedPetLike.getDesertionNo(),
                savedPetLike.getUser().getUserId(),
                true
        );
    }

    /**
     * 사용자가 반려동물 좋아요를 취소하는 메서드
     *
     * @param requestDTO 사용자의 ID와 반려동물의 desertionNo를 포함하는 요청 데이터
     * @return PetLikeResponseDTO 좋아요 취소된 반려동물의 정보가 담긴 DTO
     */
    @Override
    public PetLikeResponseDTO removePetLike(PetLikeRequestDTO requestDTO) {
        PetLike petLike = petLikeRepository.findByUserUserIdAndDesertionNo(requestDTO.getUserId(), requestDTO.getDesertionNo())
                .orElseThrow(() -> new IllegalArgumentException("좋아요 기록을 찾을 수 없습니다."));

        petLikeRepository.delete(petLike);

        return new PetLikeResponseDTO(
                petLike.getLikeId(),
                petLike.getDesertionNo(),
                petLike.getUser().getUserId(),
                false
        );
    }

    /**
     * 특정 사용자가 좋아요한 모든 반려동물의 목록을 반환하는 메서드
     *
     * @param userId 사용자의 ID
     * @return List<PetLikeResponseDTO> 사용자가 좋아요한 반려동물 목록의 DTO 리스트
     */
    @Override
    public List<PetLikeResponseDTO> getPetLikesByUser(Long userId) {
        List<PetLike> petLikes = petLikeRepository.findByUserUserId(userId);

        return petLikes.stream()
                .map(petLike -> new PetLikeResponseDTO(
                        petLike.getLikeId(),
                        petLike.getDesertionNo(),
                        petLike.getUser().getUserId(),
                        true
                ))
                .collect(Collectors.toList());
    }
}
