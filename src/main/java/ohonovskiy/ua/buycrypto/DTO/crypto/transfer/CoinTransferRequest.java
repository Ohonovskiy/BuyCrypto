package ohonovskiy.ua.buycrypto.DTO.crypto.transfer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoinTransferRequest extends TransferRequest {
    private String coinName;
}
