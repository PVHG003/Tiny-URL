package vn.pvhg.tinyurl.dto.request;

public record UserLoginRequest(
        String username,
        String password
) {
}
