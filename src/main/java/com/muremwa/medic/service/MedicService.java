package com.muremwa.medic.service;


import com.muremwa.medic.models.priadResponses.Diagnosis;
import com.muremwa.medic.models.priadResponses.Symptom;
import com.muremwa.medic.models.priadResponses.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class MedicService {
    private final String PRIAD_URL;
    private String PRIAD_AUTH_TOKEN = "";
    private final String PRIAD_AUTH_PASSWORD;
    private final RestTemplate restTemplate;
    private final Map<String, String> priadRequestParams = new HashMap<>();

    @Autowired
    public MedicService(
            RestTemplate restTemplate,
            @Value("${priad.health.url}") String priadUrl,
            @Value("${priad.auth.url}") String priadAuthUrl,
            @Value("${priad.auth.username}") String priadUsername,
            @Value("${priad.auth.password}") String priadPassword
    ) {
        this.restTemplate = restTemplate;
        this.PRIAD_URL = priadUrl;
        this.PRIAD_AUTH_PASSWORD = priadUsername + ":" + priadPassword;
        this.PRIAD_AUTH_TOKEN = this.getAuthToken(priadAuthUrl);

        // add default params for a request
        priadRequestParams.put("language", "en-gb");
        priadRequestParams.put("format", "json");
        priadRequestParams.put("token", this.PRIAD_AUTH_TOKEN);
    }

    private String priadPathBuilder(String path, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(PRIAD_URL).path(path);
        Map<String, String> localParams = params != null? params: new HashMap();
        localParams.putAll(this.priadRequestParams);

        for (String key: localParams.keySet()) {
            builder.queryParam(key, localParams.get(key));
        }
        return builder.build().toUriString();
    }

    private String getAuthToken(String authUrl) {
        if (PRIAD_AUTH_TOKEN.equals("")) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(PRIAD_AUTH_PASSWORD);
            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<TokenResponse> response = this.restTemplate.exchange(
                    authUrl,
                    HttpMethod.POST,
                    entity,
                    TokenResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                var item = response.getBody();
                return item.getToken();
            }

            return "";
        }

        return PRIAD_AUTH_TOKEN;
    }

    public List<Symptom> fetchSymptoms() {
        List<Symptom> symptoms = null;

        try {
            ResponseEntity<List<Symptom>> response = this.restTemplate.exchange(
                    this.priadPathBuilder("/symptoms", null),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Symptom>>() {}
            );

            symptoms = response.getStatusCode() == HttpStatus.OK? response.getBody(): Collections.emptyList();

        } catch (Error error) {
            System.out.println(error);
        }

        return symptoms;
    }

    public List<Diagnosis> diagnose(ArrayList<Integer> symptomIds, String gender, int yob) {
        List<Diagnosis> diagnoses = null;
        Map<String, String> diagnoseParams = new HashMap<>();
        String symptoms = "";

        for (int symptomId: symptomIds) {
            symptoms = symptoms + symptomId + ",";
        }
        diagnoseParams.put("symptoms", "[" + symptoms + "]");
        diagnoseParams.put("gender", gender);
        diagnoseParams.put("year_of_birth", String.valueOf(yob));

        System.out.println(this.priadPathBuilder("/diagnosis", diagnoseParams));

        try {
            ResponseEntity<List<Diagnosis>> response = this.restTemplate.exchange(
                    this.priadPathBuilder("/diagnosis", diagnoseParams),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Diagnosis>>() {}
            );

            diagnoses = response.getStatusCode() == HttpStatus.OK? response.getBody(): Collections.emptyList();
        } catch (HttpClientErrorException errorException) {
            System.out.println(errorException);
        }

        return diagnoses;
    }
}
