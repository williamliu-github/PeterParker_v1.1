package com.tibame.peterparker;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
        OkHttpClient client = new OkHttpClient();
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?language=en&address=" + encodedAddress)
                .get()
                .addHeader("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com")
                .addHeader("x-rapidapi-key", apiKey/*  Use your API Key here */)
                .build();
        ResponseBody responseBody = client.newCall(request).execute().body();
        return responseBody.string();
    }

    public static void main(String[] args) throws IOException {
        GeocodeController test  = new GeocodeController();
        String response = test.getGeocode("164 Townsend St. San Francisco, CA");
        System.out.println(response);
    }
}
