package vn.pvhg.tinyurl.dto.response;

import java.time.LocalDateTime;

public record AnalyticsResponse(
        String tinyUrl,
        String originalUrl,
        long visitCount,
        LocalDateTime createdAt,
        String createdBy
) {
}