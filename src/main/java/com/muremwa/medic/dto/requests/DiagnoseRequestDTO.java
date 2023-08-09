package com.muremwa.medic.dto.requests;

import lombok.Data;
import org.springframework.validation.annotation.Validated;


@Validated
@Data
public class DiagnoseRequestDTO {
    private String gender;
    private int yob;
    private int[] symptoms;
}
