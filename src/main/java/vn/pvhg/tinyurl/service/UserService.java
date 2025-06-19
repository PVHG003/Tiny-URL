package vn.pvhg.tinyurl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.pvhg.tinyurl.config.jwt.JwtUtils;
import vn.pvhg.tinyurl.dto.request.UserLoginRequest;
import vn.pvhg.tinyurl.dto.request.UserRegisterRequest;
import vn.pvhg.tinyurl.dto.response.AuthenticationResponse;
import vn.pvhg.tinyurl.model.User;
import vn.pvhg.tinyurl.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public void register(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }

        if (!request.password1().equals(request.password2())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password1()))
                .build();

        userRepository.save(user);
    }

    public AuthenticationResponse login(UserLoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String token = jwtUtils.generateToken(user);
        long expiry = jwtUtils.getTokenExpiry();

        return new AuthenticationResponse(token, expiry);
    }
}
