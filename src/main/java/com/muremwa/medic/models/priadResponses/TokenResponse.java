package com.muremwa.medic.models.priadResponses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TokenResponse {
    @JsonProperty("Token")
    private String token;

    @JsonProperty("ValidThrough")
    private int validThrough;
}
