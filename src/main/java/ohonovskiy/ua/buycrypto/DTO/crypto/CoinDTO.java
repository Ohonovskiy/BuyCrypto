package ohonovskiy.ua.buycrypto.DTO.crypto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinDTO {
    private Double price;

    private String name;

    private String imgUrl;

    private String description;
}
