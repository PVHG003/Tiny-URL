package vn.pvhg.tinyurl.dto.response;

import java.time.LocalDateTime;

public record UrlResponse(
        String tinyUrl,
        String originalUrl,
        LocalDateTime createdDate
) {
}
