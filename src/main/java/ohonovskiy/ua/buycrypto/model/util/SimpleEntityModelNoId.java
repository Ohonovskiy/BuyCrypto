package ohonovskiy.ua.buycrypto.model.util;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@ToString
@EqualsAndHashCode
@Data
public abstract class SimpleEntityModelNoId {

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Date createdOn;

    @PrePersist
    protected void onCreate() {
        createdOn = new Date();
    }

}
