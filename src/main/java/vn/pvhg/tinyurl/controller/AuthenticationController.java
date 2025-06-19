package vn.pvhg.tinyurl.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import vn.pvhg.tinyurl.dto.request.UserLoginRequest;
import vn.pvhg.tinyurl.dto.request.UserRegisterRequest;
import vn.pvhg.tinyurl.dto.response.AuthenticationResponse;
import vn.pvhg.tinyurl.service.UserService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            UriComponentsBuilder uriBuilder,
            @RequestBody UserRegisterRequest request
    ) {
        userService.register(request);
        URI uri = uriBuilder.path("/api/auth/login").build().toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserLoginRequest request) {
        AuthenticationResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
