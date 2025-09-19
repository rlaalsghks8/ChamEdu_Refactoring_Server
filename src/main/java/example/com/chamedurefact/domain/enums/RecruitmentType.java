package example.com.chamedurefact.domain.enums;

import lombok.Getter;
import java.util.*;

@Getter
public enum RecruitmentType {
    COMPREHENSIVE("학종"),
    ESSAY("논술"),
    PRACTICAL_SKILLS("실기"),
    EARLY_DECISION("정시"),
    ETC("기타");

    private final String koreanName;

    RecruitmentType(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return this.koreanName;
    }

    public static RecruitmentType fromKoreanName(String koreanName) {
        return Arrays.stream(RecruitmentType.values())
                .filter(type -> type.getKoreanName().equals(koreanName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid recruitment type korean name: " + koreanName));
    }
}
