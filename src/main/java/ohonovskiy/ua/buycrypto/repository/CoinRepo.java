package ohonovskiy.ua.buycrypto.repository;

import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepo extends JpaRepository<Coin, Long> {
}
