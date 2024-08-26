package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.ShelterInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShelterInfoService {

    /**
     * 주어진 ShelterInfo 객체를 처리하여 형식화된 데이터를 반환합니다.
     *
     * @param shelterInfo 처리할 ShelterInfo 객체
     * @return 형식화된 데이터를 담고 있는 Map 객체
     */
    public Map<String, Object> processShelterInfo(ShelterInfo shelterInfo) {
        Map<String, Object> result = new HashMap<>();

        // 보호소 이름 추가
        result.put("care_nm", shelterInfo.getCareNm());

        // 보호소 주소 추가
        result.put("care_addr", shelterInfo.getCareAddr());

        // 취급 동물 정보 가공 (플러스를 쉼표로 변경)
        result.put("save_trgt_animal", processAnimal(shelterInfo.getSaveTrgtAnimal()));

        // 평일 운영 시간 추가
        result.put("week_operating_hours", processWeekHours(shelterInfo.getWeekOprStime(), shelterInfo.getWeekOprEtime()));

        // 주말 운영 시간 추가
        result.put("weekend_operating_hours", processWeekendHours(shelterInfo.getWeekendOprStime(), shelterInfo.getWeekendOprEtime()));

        // 휴무일 추가
        result.put("close_day", shelterInfo.getCloseDay());

        // 전화번호 추가
        result.put("care_tel", shelterInfo.getCareTel());

        // 지역 추가
        result.put("org_nm", shelterInfo.getOrgNm());

        return result; // 최종 처리된 데이터를 Map으로 반환
    }

    /**
     * 취급 동물 정보를 가공하여 반환하는 메서드.
     * "+" 기호를 "," 기호로 변경합니다.
     *
     * @param saveTrgtAnimal 처리할 취급 동물 정보 문자열
     * @return 가공된 취급 동물 정보 문자열
     */
    private String processAnimal(String saveTrgtAnimal) {
        if (saveTrgtAnimal == null) {
            return "정보 없음";
        }
        return saveTrgtAnimal.replace("+", ",");
    }

    /**
     * 평일 운영 시간을 가공하여 반환하는 메서드.
     * 시작 시간과 종료 시간을 "HH:mm ~ HH:mm" 형식으로 조합합니다.
     *
     * @param startTime 평일 운영 시작 시간
     * @param endTime 평일 운영 종료 시간
     * @return 가공된 평일 운영 시간 문자열 또는 "운영 시간 없음"
     */
    private String processWeekHours(String startTime, String endTime) {
        if (startTime != null && endTime != null && !startTime.isEmpty() && !endTime.isEmpty()) {
            return startTime + " ~ " + endTime;
        }
        return "운영 시간 없음";
    }

    /**
     * 주말 운영 시간을 가공하여 반환하는 메서드.
     * 시작 시간과 종료 시간을 "HH:mm ~ HH:mm" 형식으로 조합합니다.
     *
     * @param startTime 주말 운영 시작 시간
     * @param endTime 주말 운영 종료 시간
     * @return 가공된 주말 운영 시간 문자열 또는 "운영 안함"
     */
    private String processWeekendHours(String startTime, String endTime) {
        if (startTime != null && endTime != null && !startTime.isEmpty() && !endTime.isEmpty()) {
            return startTime + " ~ " + endTime;
        }
        return "운영 안함";
    }
}
