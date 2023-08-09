package com.muremwa.medic.controllers;

import com.muremwa.medic.models.priadResponses.Diagnosis;
import com.muremwa.medic.models.priadResponses.Symptom;
import com.muremwa.medic.dto.requests.DiagnoseRequestDTO;
import com.muremwa.medic.dto.responses.MainResponseDTO;
import com.muremwa.medic.service.MedicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/medic")
public class MedicController {

    private final MedicService service;

    @Autowired
    public MedicController(MedicService service) {
        this.service = service;
    }

    @GetMapping("/symptoms")
    public ResponseEntity<MainResponseDTO<Symptom>> getSymptoms() {
        var response = MainResponseDTO.<Symptom>builder();
        HttpStatusCode code = HttpStatus.OK;

        try {
            List<Symptom> symptoms = service.fetchSymptoms();
            response.message("Fetched symptoms").success(true).data(symptoms);
        } catch (Exception exception) {
            response.message("Could not fetch symptoms").success(false);
            code = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return ResponseEntity.status(code).body(response.build());
    }

    @GetMapping("/diagnose")
    public ResponseEntity<MainResponseDTO<Diagnosis>> getDiagnosis(@ModelAttribute DiagnoseRequestDTO diagnoseRequest) {
        var response = MainResponseDTO.<Diagnosis>builder();
        HttpStatusCode code = HttpStatus.OK;
        List<Integer> symIds = Arrays.stream(diagnoseRequest.getSymptoms()).boxed().toList();
        List<String> genders = Arrays.asList("male", "female");
        String gender = diagnoseRequest.getGender();
        int year = LocalDate.now().getYear();
        int yearOfBirth = diagnoseRequest.getYob();

        if (year > yearOfBirth && yearOfBirth > (year - 100) && genders.contains(gender.toLowerCase())) {
            try {
                var diagnosis = service.diagnose(symIds, gender, yearOfBirth);
                response.message("Fetched diagnosis").success(true).data(diagnosis);
            } catch (Exception exception) {
                code = HttpStatus.INTERNAL_SERVER_ERROR;
                response.message("Could not diagnose").success(false);
            }
        } else {
            response.message("parameter provided are incorrect").success(false);
            code = HttpStatus.BAD_REQUEST;
        }

        return ResponseEntity.status(code).body(response.build());
    }
}
