package luckyvicky.petharmony;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PetharmonyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetharmonyApplication.class, args);
	}
}
