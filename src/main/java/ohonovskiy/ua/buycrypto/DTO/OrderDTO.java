package ohonovskiy.ua.buycrypto.DTO;

import lombok.Data;
import lombok.ToString;
import ohonovskiy.ua.buycrypto.enums.OrderType;

@Data
@ToString
public class OrderDTO {

    private String coinName;

    private Double amount;

    private Double price;

    private OrderType orderType;
}
