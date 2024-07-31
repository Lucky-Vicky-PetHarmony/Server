package luckyvicky.petharmony.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adopter_info")
public class AdopterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 입양자 이름
    @Column(name = "name", length = 100)
    private String name;

    // 이메일
    @Column(name = "email", length = 100)
    private String email;

    // 비밀번호
    @Column(name = "password", length = 255)
    private String password;

    // 생년월일
    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    // 전화번호
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    // 성별
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    // 주소 (우편번호)
    @Column(name = "address", length = 255)
    private String address;

    // 선호 입양동물 특징 단어
    @Enumerated(EnumType.STRING)
    @Column(name = "favorite_word")
    private FavoriteWord favoriteWord;

    // Getter 및 Setter 메소드
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public FavoriteWord getFavoriteWord() {
        return favoriteWord;
    }

    public void setFavoriteWord(FavoriteWord favoriteWord) {
        this.favoriteWord = favoriteWord;
    }

    // Enum 클래스
    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum FavoriteWord {
        건강한, 약한, 회복중인, 튼튼한, 활발한, 차분한, 순함, 호기심많음, 사교적인, 내성적인, 예쁜, 귀여운, 멋진, 평범한, 사랑스러운, 조용한, 호기심많은, 특별한, 독특한, 일반적인, 눈에띄는
    }
}
