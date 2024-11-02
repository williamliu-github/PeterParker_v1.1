package com.tibame.peterparker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tibame.peterparker.dto.GeocodeResult;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;

@RestController
public class GeocodeController {

    @Value("${google.api.key}")
    private String apiKey;

    @RequestMapping(path = "/geocode", method = RequestMethod.GET )
    public GeocodeResult getGeocode(@RequestParam String address) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?llanguage=en&address=" + encodedAddress)
                .get()
                .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                .addHeader("x-rapidapi-key", apiKey/*  Use your API Key here */)
                .build();

//        ResponseBody responseBody = client.newCall(request).execute().body();
//        ObjectMapper objectMapper = new ObjectMapper();
//        GeocodeResult result = objectMapper.readValue(responseBody.string(), GeocodeResult.class);
//        return result;

       try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(responseBody.string(), GeocodeResult.class);
            } else {
                throw new IOException("Empty response body");
            }
        }

    }
}