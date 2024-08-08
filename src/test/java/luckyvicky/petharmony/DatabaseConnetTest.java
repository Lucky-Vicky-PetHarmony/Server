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
class DatabaseConnectionTest {

    @Autowired
    private PetInfoRepository petInfoRepository;

    /**
     * 이 테스트는 PetInfo 엔티티를 데이터베이스에 저장하고,
     * 저장된 데이터를 올바르게 조회할 수 있는지 검증합니다.
     */
    @Test
    @DisplayName("정상 PetInfo 저장 및 조회 테스트")
    @Transactional
    void testSaveAndFindPetInfo() {
        // given: 테스트를 위한 PetInfo 객체를 생성합니다.
        PetInfo pet = PetInfo.builder()
                .desertion_no("12345")
                .filename("test.jpg")
                .happen_dt(new Date())
                .happen_place("Test Place")
                .kind_cd("Dog")
                .color_cd("Brown")
                .age("2 years")
                .weight("5 kg")
                .notice_no("Notice123")
                .notice_sdt(new Date())
                .notice_edt(new Date())
                .popfile("popfile.jpg")
                .process_state("Adopted")
                .sex_cd('M')
                .neuter_yn('Y')
                .special_mark("No marks")
                .care_nm("Care Center")
                .charge_nm("John Doe")
                .officetel("123-456-7890")
                .notice_comment("No comments")
                .build();

        // when: PetInfo 객체를 데이터베이스에 저장하고, 저장된 객체를 다시 조회합니다.
        PetInfo insertedPet = petInfoRepository.save(pet);
        PetInfo foundPet = petInfoRepository.findById(insertedPet.getDesertion_no()).orElse(null);

        // then: 조회한 PetInfo 객체가 null이 아니고, 저장한 객체와 동일한지 검증합니다.
        assertThat(foundPet).isNotNull();
        assertThat(foundPet.getDesertion_no()).isEqualTo(insertedPet.getDesertion_no());
        assertThat(foundPet.getFilename()).isEqualTo(insertedPet.getFilename());
        assertThat(foundPet.getHappen_dt()).isEqualTo(insertedPet.getHappen_dt());
        assertThat(foundPet.getHappen_place()).isEqualTo(insertedPet.getHappen_place());
        assertThat(foundPet.getKind_cd()).isEqualTo(insertedPet.getKind_cd());
        assertThat(foundPet.getColor_cd()).isEqualTo(insertedPet.getColor_cd());
        assertThat(foundPet.getAge()).isEqualTo(insertedPet.getAge());
        assertThat(foundPet.getWeight()).isEqualTo(insertedPet.getWeight());
        assertThat(foundPet.getNotice_no()).isEqualTo(insertedPet.getNotice_no());
        assertThat(foundPet.getNotice_sdt()).isEqualTo(insertedPet.getNotice_sdt());
        assertThat(foundPet.getNotice_edt()).isEqualTo(insertedPet.getNotice_edt());
        assertThat(foundPet.getPopfile()).isEqualTo(insertedPet.getPopfile());
        assertThat(foundPet.getProcess_state()).isEqualTo(insertedPet.getProcess_state());
        assertThat(foundPet.getSex_cd()).isEqualTo(insertedPet.getSex_cd());
        assertThat(foundPet.getNeuter_yn()).isEqualTo(insertedPet.getNeuter_yn());
        assertThat(foundPet.getSpecial_mark()).isEqualTo(insertedPet.getSpecial_mark());
        assertThat(foundPet.getCare_nm()).isEqualTo(insertedPet.getCare_nm());
        assertThat(foundPet.getCharge_nm()).isEqualTo(insertedPet.getCharge_nm());
        assertThat(foundPet.getOfficetel()).isEqualTo(insertedPet.getOfficetel());
        assertThat(foundPet.getNotice_comment()).isEqualTo(insertedPet.getNotice_comment());
    }
}
