package vn.pvhg.tinyurl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.pvhg.tinyurl.dto.request.UrlRequest;
import vn.pvhg.tinyurl.dto.response.UrlResponse;
import vn.pvhg.tinyurl.model.TinyUrl;
import vn.pvhg.tinyurl.model.User;
import vn.pvhg.tinyurl.model.VisitLog;
import vn.pvhg.tinyurl.repository.TinyUrlRepository;
import vn.pvhg.tinyurl.repository.VisitLogRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TinyUrlService {

    private final TinyUrlRepository tinyUrlRepository;
    private final VisitLogRepository visitLogRepository;
    @Value("${app.base-url}")
    private String baseUrl;

    public UrlResponse minifyUrl(UrlRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            user = (User) authentication.getPrincipal();
        }

        if (user != null) {
            Optional<TinyUrl> urlExistForUser = tinyUrlRepository.findByOriginalUrlAndUser(request.originalUrl(), user);
            if (urlExistForUser.isPresent()) {
                TinyUrl tinyUrl = urlExistForUser.get();
                if (tinyUrl.getExpirationDate() == null || tinyUrl.getExpirationDate().isAfter(LocalDateTime.now())) {
                    return new UrlResponse(
                            urlExistForUser.get().getOriginalUrl(),
                            baseUrl + "/api/public/" + urlExistForUser.get().getTinyUrl(),
                            urlExistForUser.get().getCreatedDate()
                    );
                }
            }
        }

        String minifyUrlCode;
        do {
            minifyUrlCode = UUID.randomUUID().toString().substring(0, 8);
        } while (tinyUrlRepository.existsByTinyUrl(minifyUrlCode));

        TinyUrl tinyUrl = TinyUrl.builder()
                .originalUrl(request.originalUrl())
                .tinyUrl(minifyUrlCode)
                .user(user)
                .expirationDate(LocalDateTime.now().plusDays(7))
                .build();

        tinyUrlRepository.save(tinyUrl);

        return new UrlResponse(
                request.originalUrl(),
                baseUrl + "/api/public/" + minifyUrlCode,
                tinyUrl.getCreatedDate()
        );
    }

    public String resolveOriginalUrl(String shortenCode) {
        TinyUrl tinyUrl = tinyUrlRepository.findByTinyUrl(shortenCode)
                .orElseThrow(() -> new NoSuchElementException("TinyUrl not found"));

        if (tinyUrl.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("TinyUrl expired");
        }

        tinyUrl.setVisitCount(tinyUrl.getVisitCount() + 1);
        visitLogRepository.save(VisitLog.builder()
                .tinyUrl(tinyUrl)
                .visitTime(LocalDateTime.now())
                .build());

        tinyUrlRepository.save(tinyUrl);

        return tinyUrl.getOriginalUrl();
    }
}
