package luckyvicky.petharmony;

import luckyvicky.petharmony.entity.PetInfo;
import luckyvicky.petharmony.repository.PetInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class PetHarmonyApplicationTests {

	@Autowired
	private PetInfoRepository petInfoRepository;

	@Test
	@DisplayName("정상 PetInfo 저장 및 조회 테스트")
	@Transactional
	void testSaveAndFindPetInfo() {
		// given
		PetInfo pet = new PetInfo();
		pet.setDesertionNo("12345");
		pet.setFilename("test.jpg");
		pet.setHappenDt(new Date());
		pet.setHappenPlace("Test Place");
		pet.setKindCd("Dog");
		pet.setColorCd("Brown");
		pet.setAge("2 years");
		pet.setWeight("5 kg");
		pet.setNoticeNo("Notice123");
		pet.setNoticeSdt(new Date());
		pet.setNoticeEdt(new Date());
		pet.setPopfile("popfile.jpg");
		pet.setProcessState("Adopted");
		pet.setSexCd('M');
		pet.setNeuterYn('Y');
		pet.setSpecialMark("No marks");
		pet.setCareNm("Care Center");
		pet.setChargeNm("John Doe");
		pet.setOfficetel("123-456-7890");
		pet.setNoticeComment("No comments");

		// when
		PetInfo insertedPet = petInfoRepository.save(pet);
		PetInfo foundPet = petInfoRepository.findById(insertedPet.getDesertionNo()).orElse(null);

		// then
		assertThat(foundPet).isNotNull();
		assertThat(foundPet.getDesertionNo()).isEqualTo(insertedPet.getDesertionNo());
		assertThat(foundPet.getFilename()).isEqualTo(insertedPet.getFilename());
		assertThat(foundPet.getHappenDt()).isEqualTo(insertedPet.getHappenDt());
		assertThat(foundPet.getHappenPlace()).isEqualTo(insertedPet.getHappenPlace());
		assertThat(foundPet.getKindCd()).isEqualTo(insertedPet.getKindCd());
		assertThat(foundPet.getColorCd()).isEqualTo(insertedPet.getColorCd());
		assertThat(foundPet.getAge()).isEqualTo(insertedPet.getAge());
		assertThat(foundPet.getWeight()).isEqualTo(insertedPet.getWeight());
		assertThat(foundPet.getNoticeNo()).isEqualTo(insertedPet.getNoticeNo());
		assertThat(foundPet.getNoticeSdt()).isEqualTo(insertedPet.getNoticeSdt());
		assertThat(foundPet.getNoticeEdt()).isEqualTo(insertedPet.getNoticeEdt());
		assertThat(foundPet.getPopfile()).isEqualTo(insertedPet.getPopfile());
		assertThat(foundPet.getProcessState()).isEqualTo(insertedPet.getProcessState());
		assertThat(foundPet.getSexCd()).isEqualTo(insertedPet.getSexCd());
		assertThat(foundPet.getNeuterYn()).isEqualTo(insertedPet.getNeuterYn());
		assertThat(foundPet.getSpecialMark()).isEqualTo(insertedPet.getSpecialMark());
		assertThat(foundPet.getCareNm()).isEqualTo(insertedPet.getCareNm());
		assertThat(foundPet.getChargeNm()).isEqualTo(insertedPet.getChargeNm());
		assertThat(foundPet.getOfficetel()).isEqualTo(insertedPet.getOfficetel());
		assertThat(foundPet.getNoticeComment()).isEqualTo(insertedPet.getNoticeComment());
	}
}
