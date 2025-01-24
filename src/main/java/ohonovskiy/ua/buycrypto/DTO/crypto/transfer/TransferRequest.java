package ohonovskiy.ua.buycrypto.DTO.crypto.transfer;

import lombok.Data;

@Data
public abstract class TransferRequest {
    Long userId;
    Double amount;
}
