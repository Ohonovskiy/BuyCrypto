package ohonovskiy.ua.buycrypto.service;

import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.repository.CoinRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoinService {

    private final CoinRepo coinRepo;

    public CoinService(CoinRepo coinRepo) {
        this.coinRepo = coinRepo;
    }

    public Long getUpdatedPrice(String coinName) {
        return new RestTemplate()
                .getForObject(
                        String.format("https://api.coinbase.com/v2/prices/%s-USDT/buy", coinName),
                        Long.class);
    }

    public Coin updatePrice(Coin coin) {
        coin.setPrice(
                getUpdatedPrice(coin.getName())
        );
        return coinRepo.save(coin);
    }
}
