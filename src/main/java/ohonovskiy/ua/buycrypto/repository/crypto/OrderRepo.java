package ohonovskiy.ua.buycrypto.repository.crypto;

import ohonovskiy.ua.buycrypto.model.crypto.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
