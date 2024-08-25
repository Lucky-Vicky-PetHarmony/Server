package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetLike;
import luckyvicky.petharmony.service.PetLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PetLikeController {

    private final PetLikeService petLikeService;

    @Autowired
    public PetLikeController(PetLikeService petLikeService) {
        this.petLikeService = petLikeService;
    }

    @PostMapping("/user/pet-likes")
    public PetLike likePet(@RequestParam Long userId, @RequestParam String desertionNo) {
        return petLikeService.savePetLike(userId, desertionNo);
    }
}
