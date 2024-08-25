package luckyvicky.petharmony.service;

import luckyvicky.petharmony.entity.PetInfo;

import java.util.Map;

public interface PetInfoFormatter {
    Map<String, Object> processPetInfo(PetInfo petInfo);
}
