package vn.pvhg.tinyurl.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.pvhg.tinyurl.dto.request.UrlRequest;
import vn.pvhg.tinyurl.dto.response.UrlResponse;
import vn.pvhg.tinyurl.service.TinyUrlService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class TinyUrlController {

    @Value("${app.base-url}")
    private String baseUrl;

    private final TinyUrlService tinyUrlService;

    @PostMapping("/minify")
    public ResponseEntity<UrlResponse> minifyUrl(@RequestBody UrlRequest request) {
        UrlResponse response = tinyUrlService.minifyUrl(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{minify-code}")
    public ResponseEntity<?> redirectToOriginalUrl(
            @PathVariable("minify-code") String shortenCode,
            HttpServletResponse response
    ) throws IOException {
        String originalUrl = tinyUrlService.resolveOriginalUrl(shortenCode);
        response.sendRedirect(originalUrl);
        return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).build();
    }

    @GetMapping("/qr/{minify-code}")
    public ResponseEntity<byte[]> redirectToOriginalUrlWithQr(
            @PathVariable("minify-code") String minifyCode
    ) {
        String targetUrl = baseUrl + "/api/public/" + minifyCode;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    targetUrl,
                    BarcodeFormat.QR_CODE,
                    200,
                    200
            );
            MatrixToImageWriter.writeToStream(bitMatrix, "png", outputStream);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(outputStream.toByteArray());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
