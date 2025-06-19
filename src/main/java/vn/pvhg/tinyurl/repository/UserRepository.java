package vn.pvhg.tinyurl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.pvhg.tinyurl.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
