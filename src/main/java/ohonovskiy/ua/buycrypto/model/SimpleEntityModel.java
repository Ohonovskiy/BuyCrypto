package ohonovskiy.ua.buycrypto.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@ToString
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class SimpleEntityModel extends SimpleEntityModelNoId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
