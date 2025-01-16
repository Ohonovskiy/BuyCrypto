package ohonovskiy.ua.buycrypto.DTO.transfer;

import lombok.Data;

@Data
public abstract class TransferRequest {
    Long userId;
    Double amount;
}
