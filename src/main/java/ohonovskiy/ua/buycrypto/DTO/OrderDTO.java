package ohonovskiy.ua.buycrypto.DTO;

import lombok.Data;
import ohonovskiy.ua.buycrypto.enums.OrderType;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;

@Data
public class OrderDTO {

    private Coin coin;

    private Double amount;

    private Double price;

    private OrderType orderType;
}
