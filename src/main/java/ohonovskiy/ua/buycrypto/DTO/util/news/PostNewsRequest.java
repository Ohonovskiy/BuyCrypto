package ohonovskiy.ua.buycrypto.DTO.util.news;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostNewsRequest {
    private String message;
}
