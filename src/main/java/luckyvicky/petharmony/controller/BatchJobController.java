package luckyvicky.petharmony.controller;

import luckyvicky.petharmony.config.PetInfoAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 필요시에만 배치작업을 실행할 수 있도록 하는 Controller
 */
@RestController
@RequestMapping("/api/batch")
public class BatchJobController {

    private final PetInfoAnalyzer petInfoAnalyzer;

    @Autowired
    public BatchJobController(PetInfoAnalyzer petInfoAnalyzer) {
        this.petInfoAnalyzer = petInfoAnalyzer;
    }

    @PostMapping("/run")
    public ResponseEntity<String> runBatchJob() {
        petInfoAnalyzer.runJob();
        return ResponseEntity.ok("배치 작업이 실행되었습니다.");
    }
}
