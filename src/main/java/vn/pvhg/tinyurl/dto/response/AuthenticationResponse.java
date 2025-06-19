package vn.pvhg.tinyurl.dto.response;

public record AuthenticationResponse(
        String token,
        long expiry
) {
}
