package ohonovskiy.ua.buycrypto.model.chart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ohonovskiy.ua.buycrypto.model.SimpleEntityModel;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import org.hibernate.annotations.BatchSize;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@BatchSize(size = 5)
public class Chart extends SimpleEntityModel {

    // use parent createdOn for chart time

    private Double price;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Coin coin;

    @Override
    public String toString() {
        return "Chart{}";
    }
}
