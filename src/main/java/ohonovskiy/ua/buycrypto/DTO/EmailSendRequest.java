package ohonovskiy.ua.buycrypto.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class EmailSendRequest {

    private String fullName;

    private String email;

    private String message;
}
