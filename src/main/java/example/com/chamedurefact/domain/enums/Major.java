package example.com.chamedurefact.domain.enums;

import lombok.Getter;

import java.util.*;

@Getter
public enum Major {
    LIBERAL_ARTS("인문사회"),
    COMMERCE_AND_ECONOMICS("경상"),
    BUSINESS("경영"),
    NATURAL_SCIENCE("자연과학"),
    ENGINEERING_IT("공학IT"),
    ARTS_AND_PHYSICAL_EDUCATION("예체능"),
    ETC("기타");

    private final String koreanName;

    Major(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return this.koreanName;
    }

    public static Major fromKoreanName(String koreanName) {
        return Arrays.stream(Major.values())
                .filter(major -> major.getKoreanName().equals(koreanName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid major korean name: " + koreanName));
    }
}
