package utcluj.stiinte.bloodchain.data.authentication;

public record LoginResponse(String token,
                            long id,
                            String email,
                            String role) {
}