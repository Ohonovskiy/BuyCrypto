package ohonovskiy.ua.buycrypto.model.chart;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ohonovskiy.ua.buycrypto.model.SimpleEntityModel;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
public class Chart extends SimpleEntityModel {

    // use parent createdOn for chart time

    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    private Coin coin;

    @Override
    public String toString() {
        return "Chart{}";
    }
}
