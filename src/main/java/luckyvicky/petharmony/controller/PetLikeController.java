package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.dto.PetLikeRequestDTO;
import luckyvicky.petharmony.dto.PetLikeResponseDTO;
import luckyvicky.petharmony.service.PetLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class PetLikeController {

    private final PetLikeService petLikeService;

    @Autowired
    public PetLikeController(PetLikeService petLikeService) {
        this.petLikeService = petLikeService;
    }

    /**
     * 반려동물 좋아요 처리 또는 좋아요 취소 요청을 처리
     *
     * @param requestDTO 사용자의 ID, 반려동물의 desertionNo, 좋아요 여부를 포함하는 요청 데이터
     * @return 좋아요 또는 좋아요 취소 결과를 담은 PetLikeResponseDTO 객체
     */
    @PostMapping("/pet-likes")
    public ResponseEntity<PetLikeResponseDTO> likeOrUnlikePet(@RequestBody PetLikeRequestDTO requestDTO) {
        PetLikeResponseDTO responseDTO;

        // 좋아요 또는 좋아요 취소 요청을 처리
        if (requestDTO.isLiked()) {
            responseDTO = petLikeService.savePetLike(requestDTO);
        } else {
            responseDTO = petLikeService.removePetLike(requestDTO);
        }

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * 특정 사용자가 좋아요한 반려동물 목록을 반환
     *
     * @param userId 사용자의 ID
     * @return 사용자가 좋아요한 반려동물 목록을 담은 PetLikeResponseDTO 리스트
     */
    @GetMapping("/{userId}/pet-likes")
    public ResponseEntity<List<PetLikeResponseDTO>> getPetLikesByUser(@PathVariable Long userId) {
        List<PetLikeResponseDTO> petLikes = petLikeService.getPetLikesByUser(userId);
        return ResponseEntity.ok(petLikes);
    }
}
