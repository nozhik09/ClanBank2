package org.bankSystem.network;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyNetworkWorker {
    private final HttpClient client = HttpClient.newHttpClient();
    private String baseUrl = "https://api.frankfurter.app";

    public LatestCurrencyResponse requestLatestCurrency() throws IOException, InterruptedException {
        String url = baseUrl + "/latest";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), LatestCurrencyResponse.class);
    }

}