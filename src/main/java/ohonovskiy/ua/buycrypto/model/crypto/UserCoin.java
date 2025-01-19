package ohonovskiy.ua.buycrypto.model.crypto;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ohonovskiy.ua.buycrypto.model.util.SimpleEntityModel;
import ohonovskiy.ua.buycrypto.model.user.User;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@ToString
public class UserCoin extends SimpleEntityModel {

    @ManyToOne
    private User user;

    @ManyToOne
    private Coin coin;

    private Double amount;
}
