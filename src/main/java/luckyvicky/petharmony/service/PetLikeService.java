package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.entity.User;
import luckyvicky.petharmony.repository.PetLikeRepository;
import luckyvicky.petharmony.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetLikeService {

    private final PetLikeRepository petLikeRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetLikeService(PetLikeRepository petLikeRepository, UserRepository userRepository) {
        this.petLikeRepository = petLikeRepository;
        this.userRepository = userRepository;
    }

    public PetLike savePetLike(Long userId, String desertionNo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        PetLike petLike = PetLike.builder()
                .user(user)
                .desertionNo(desertionNo)
                .build();
        return petLikeRepository.save(petLike);
    }
}
