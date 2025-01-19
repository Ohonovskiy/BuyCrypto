package ohonovskiy.ua.buycrypto.repository.util;

import ohonovskiy.ua.buycrypto.model.util.news.NewsSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsRepo extends JpaRepository<NewsSubscriber, Long> {
    void deleteByEmail(String email);
    Optional<NewsSubscriber> findNewsSubscriberByEmail(String email);
}
