package luckyvicky.petharmony.config;

import luckyvicky.petharmony.service.PetInfoWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PetInfoAnalyzer implements CommandLineRunner {

    private final PetInfoWordService petInfoWordService;

    @Autowired
    public PetInfoAnalyzer(PetInfoWordService petInfoWordService) {
        this.petInfoWordService = petInfoWordService;
    }

    @Override
    public void run(String... args) {
        petInfoWordService.processTop5PetInfo();
    }
}
