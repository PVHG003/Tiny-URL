package vn.pvhg.tinyurl.dto.request;

public record UserRegisterRequest(
        String username,
        String password1,
        String password2
) {
}
