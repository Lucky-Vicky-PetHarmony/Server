package luckyvicky.petharmony;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "luckyvicky.petharmony.repository")
@EntityScan(basePackages = "luckyvicky.petharmony.entity")
@ComponentScan(basePackages = "luckyvicky.petharmony")
public class PetharmonyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetharmonyApplication.class, args);
	}
}
