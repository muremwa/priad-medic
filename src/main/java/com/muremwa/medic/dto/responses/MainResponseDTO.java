package com.muremwa.medic.dto.responses;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class MainResponseDTO<T> {
    private boolean success;
    private String message;
    private List<T> data;
}
