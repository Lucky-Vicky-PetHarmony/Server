package luckyvicky.petharmony.service;

import luckyvicky.petharmony.dto.PetLikeRequestDTO;
import luckyvicky.petharmony.dto.PetLikeResponseDTO;

import java.util.List;

public interface PetLikeService {
    PetLikeResponseDTO savePetLike(PetLikeRequestDTO requestDTO);    // 좋아요 처리 메서드
    PetLikeResponseDTO removePetLike(PetLikeRequestDTO requestDTO);  // 좋아요 취소 메서드
    List<PetLikeResponseDTO> getPetLikesByUser(Long userId);         // 사용자가 좋아요한 목록 조회 메서드
}
