package com.muremwa.medic.models.priadResponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Diagnosis {
    @JsonProperty("Issue")
    private Issue issue;

    @JsonProperty("Specialisation")
    private List<SpecialisationItem> specialisation;

    @Data
    @NoArgsConstructor
    public static class Issue {
        @JsonProperty("ID")
        private int id;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Accuracy")
        private double accuracy;

        @JsonProperty("Icd")
        private String icd;

        @JsonProperty("IcdName")
        private String icdName;

        @JsonProperty("ProfName")
        private String profName;

        @JsonProperty("Ranking")
        private int ranking;
    }

    @Data
    @NoArgsConstructor
    public static class SpecialisationItem {
        @JsonProperty("ID")
        private int id;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("SpecialistID")
        private int specialistId;
    }
}
