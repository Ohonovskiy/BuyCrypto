package ohonovskiy.ua.buycrypto.DTO.transfer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CoinTransferRequest extends TransferRequest {
    private String coinName;
}
