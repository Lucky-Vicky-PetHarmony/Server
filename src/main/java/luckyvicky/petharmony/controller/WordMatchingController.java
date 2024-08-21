package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.service.WordMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WordMatchingController {

    private final WordMatchingService wordMatchingService;

    @Autowired
    public WordMatchingController(WordMatchingService wordMatchingService) {
        this.wordMatchingService = wordMatchingService;
    }

    @GetMapping("/user/top12/{userId}")
    public List<PetInfo> getTop12PetsByUserWord(@PathVariable Long userId) {
        return wordMatchingService.getTop12PetInfosByUserWord(userId);
    }

}
