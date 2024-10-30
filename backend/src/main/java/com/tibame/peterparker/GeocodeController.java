package com.tibame.peterparker;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;

@RestController
public class GeocodeController {

    @Value("${google.api.key}")
    private String apiKey;

    @RequestMapping(path = "/geocode", method = RequestMethod.GET )
    public String getGeocode(@RequestParam String address) throws IOException {

        // 驗證 API Key 是否已設定
        if (apiKey == null || apiKey.isEmpty()) {
            return "{\"error\": \"API key is missing or invalid\"}";
        }

        OkHttpClient client = new OkHttpClient();
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?language=en&address=" + encodedAddress)
                .get()
                .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                .addHeader("x-rapidapi-key", apiKey/*  Use your API Key here */)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "{\"error\": \"Request failed with status: " + response.code() + "\"}";
            }

            try (ResponseBody responseBody = response.body()) {
                if (responseBody != null) {
                    return responseBody.string();
                } else {
                    return "{\"error\": \"Empty response body\"}";
                }
            }
        }

    }
}