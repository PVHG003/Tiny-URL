package vn.pvhg.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.pvhg.tinyurl.dto.response.AnalyticsResponse;
import vn.pvhg.tinyurl.repository.TinyUrlRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final TinyUrlRepository tinyUrlRepository;

    @GetMapping("/analytics")
    public ResponseEntity<List<AnalyticsResponse>> getAllUrlAnalytics() {
        List<AnalyticsResponse> responses = tinyUrlRepository.findAll().stream()
                .map(url -> new AnalyticsResponse(
                        url.getTinyUrl(),
                        url.getOriginalUrl(),
                        url.getVisitCount(),
                        url.getCreatedDate(),
                        url.getUser() != null ? url.getUser().getUsername() : "ANONYMOUS"
                ))
                .toList();
        return ResponseEntity.ok(responses);
    }
}
