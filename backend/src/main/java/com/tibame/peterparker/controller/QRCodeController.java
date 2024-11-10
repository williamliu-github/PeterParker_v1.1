package com.tibame.peterparker.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/qr")
public class QRCodeController {

    // 使用 @Value 來從 application.properties 中獲取基礎 URL
    @Value("${app.base-url:http://localhost:8081}")
    private String baseUrl;

    @GetMapping("/generate")
    public ResponseEntity<ByteArrayResource> generateQRCode(@RequestParam String orderId) {
        try {
            // 檢查 orderId 是否為空或無效
            if (orderId == null || orderId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

            // 動態生成 URL
            String url = baseUrl + "/order/scan?orderId=" + orderId;

            // 使用 ZXing 生成 QR Code
            BitMatrix matrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, 300, 300);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);

            byte[] imageData = outputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(imageData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        } catch (Exception e) {
            // 更詳細的異常處理
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
