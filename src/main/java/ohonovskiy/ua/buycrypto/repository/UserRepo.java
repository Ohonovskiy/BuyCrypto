package ohonovskiy.ua.buycrypto.repository;

import ohonovskiy.ua.buycrypto.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findFirstByUsername(String username);
}
