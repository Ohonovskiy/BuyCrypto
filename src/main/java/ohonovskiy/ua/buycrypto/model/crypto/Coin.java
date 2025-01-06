package ohonovskiy.ua.buycrypto.model.crypto;

import jakarta.persistence.Entity;
import lombok.*;
import ohonovskiy.ua.buycrypto.model.SimpleEntityModel;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@ToString
public class Coin extends SimpleEntityModel {
    private String name;

    private Double price;
}
