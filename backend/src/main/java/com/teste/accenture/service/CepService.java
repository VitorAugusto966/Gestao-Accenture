package com.teste.accenture.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CepService {

    private static final String BASE_URL = "http://cep.la/";
    private final RestTemplate restTemplate;

    public CepService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean isCepValido(String cep) {
        String json = buscarCepJson(cep);
        return json != null && !json.isBlank();
    }

    public boolean isEmpresaDoParana(String cep) {
        String json = buscarCepJson(cep);
        return json != null && json.toLowerCase().contains("\"uf\":\"pr\"");
    }

    private String buscarCepJson(String cep) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", "application/json");

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            var response = restTemplate.exchange(
                    BASE_URL + cep,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    String.class);

            return response.getStatusCode().is2xxSuccessful() ? response.getBody() : null;
        } catch (Exception e) {
            return null;
        }
    }

}
