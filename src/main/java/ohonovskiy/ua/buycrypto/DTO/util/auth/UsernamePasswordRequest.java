package ohonovskiy.ua.buycrypto.DTO.util.auth;

import lombok.Data;

@Data
public class UsernamePasswordRequest {
    private String username;
    private String password;
}
