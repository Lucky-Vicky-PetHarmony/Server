package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.dto.PetLikeRequestDTO;
import luckyvicky.petharmony.dto.PetLikeResponseDTO;
import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.service.PetLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PetLikeController {

    private final PetLikeService petLikeService;

    @Autowired
    public PetLikeController(PetLikeService petLikeService) {
        this.petLikeService = petLikeService;
    }

    /**
     * 사용자가 반려동물을 좋아요 표시하는 요청을 처리합니다.
     *
     * @param requestDTO 사용자의 ID와 반려동물의 desertionNo를 포함하는 요청 데이터
     * @return PetLikeResponseDTO 좋아요가 성공적으로 저장된 후 반환되는 데이터
     */
    @PostMapping("/user/pet-likes")
    public PetLikeResponseDTO likePet(@RequestBody PetLikeRequestDTO requestDTO) {
        // PetLikeService의 savePetLike 메서드에 DTO를 그대로 전달합니다.
        PetLike petLike = petLikeService.savePetLike(requestDTO);
        return new PetLikeResponseDTO(
                petLike.getLikeId(),
                petLike.getDesertionNo(),
                petLike.getUser().getUserId()
        );
    }

    /**
     * 특정 사용자가 좋아요를 표시한 모든 반려동물의 목록을 반환합니다.
     *
     * @param userId 사용자의 ID
     * @return List<PetLikeResponseDTO> 사용자가 좋아요를 표시한 반려동물의 목록
     */
    @GetMapping("/user/{userId}/pet-likes")
    public List<PetLikeResponseDTO> getPetLikesByUser(@PathVariable Long userId) {
        List<PetLike> petLikes = petLikeService.getPetLikesByUser(userId);
        return petLikes.stream()
                .map(petLike -> new PetLikeResponseDTO(
                        petLike.getLikeId(),
                        petLike.getDesertionNo(),
                        petLike.getUser().getUserId()
                ))
                .collect(Collectors.toList());
    }
}