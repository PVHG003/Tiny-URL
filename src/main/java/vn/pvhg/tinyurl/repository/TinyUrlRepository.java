package vn.pvhg.tinyurl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.pvhg.tinyurl.model.TinyUrl;
import vn.pvhg.tinyurl.model.User;

import java.util.Optional;

public interface TinyUrlRepository extends JpaRepository<TinyUrl, Long> {
    Optional<TinyUrl> findByOriginalUrlAndUser(String originalUrl, User user);

    boolean existsByTinyUrl(String tinyUrl);

    Optional<TinyUrl> findByTinyUrl(String tinyUrl);
}
