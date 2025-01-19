package ohonovskiy.ua.buycrypto.model.util.news;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import ohonovskiy.ua.buycrypto.model.util.SimpleEntityModel;
import org.hibernate.annotations.BatchSize;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@BatchSize(size = 5)
public class NewsSubscriber extends SimpleEntityModel {
    @Column(unique = true)
    private String email;
}
