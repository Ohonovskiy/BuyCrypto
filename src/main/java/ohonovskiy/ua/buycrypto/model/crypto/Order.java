package ohonovskiy.ua.buycrypto.model.crypto;

import jakarta.persistence.*;
import lombok.*;
import ohonovskiy.ua.buycrypto.enums.OrderType;
import ohonovskiy.ua.buycrypto.model.SimpleEntityModel;
import ohonovskiy.ua.buycrypto.model.user.User;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@ToString
public class Order extends SimpleEntityModel {

    @ManyToOne
    private User user;

    @ManyToOne
    private Coin coin;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;
}
