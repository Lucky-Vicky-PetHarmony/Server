package luckyvicky.petharmony;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.service.WordMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class PetharmonyApplication implements CommandLineRunner {

	@Autowired
	private WordMatchingService wordMatchingService;

	public static void main(String[] args) {
		SpringApplication.run(PetharmonyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// user_id = 27에 대해 WordMatchingService의 메서드를 호출하여 결과를 출력합니다.
		Long userId = 27L;

		// user_id가 가지고 있는 word_id 값을 확인합니다.
		String wordIdListAsString = wordMatchingService.getWordIdListAsString(userId);
		System.out.println("Word IDs for user_id = " + userId + ": " + wordIdListAsString);

		// 매칭되는 PetInfo를 가져와서 출력합니다.
		List<PetInfo> matchingPets = wordMatchingService.getMatchingPetInfosByUserWord(userId);
		System.out.println("Matching pets for user_id = " + userId + ":");
		for (PetInfo pet : matchingPets) {
			System.out.println("DesertionNo: " + pet.getDesertionNo() + ", Words: " + pet.getWords());
		}
	}
}
