package com.muremwa.medic.models.priadResponses;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Symptom {
    @JsonProperty("ID")
    private int id;

    @JsonProperty("Name")
    private String name;
}
