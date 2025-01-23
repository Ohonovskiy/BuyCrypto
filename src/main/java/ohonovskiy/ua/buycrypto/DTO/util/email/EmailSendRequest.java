package ohonovskiy.ua.buycrypto.DTO.util.email;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ohonovskiy.ua.buycrypto.enums.EmailSendType;

@Data
@ToString
@Builder
public class EmailSendRequest {

    private String fullName;

    private String email;

    private String message;

    private EmailSendType emailSendType;
}
