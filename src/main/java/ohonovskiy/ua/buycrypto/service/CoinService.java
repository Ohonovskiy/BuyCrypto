package ohonovskiy.ua.buycrypto.service;

import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.repository.CoinRepo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class CoinService {

    private final CoinRepo coinRepo;
    private final RestTemplate restTemplate;

    private final static String[] STANDARD_COIN_NAMES =
            {
                    "BITCOIN",
                    "ETHEREUM",
                    "DOGS",
                    "RIPPLE",
                    "LITECOIN",
                    "EOS",
                    "NEO"

            };

    public CoinService(CoinRepo coinRepo, RestTemplate restTemplate) {
        this.coinRepo = coinRepo;
        this.restTemplate = restTemplate;
    }

    // execute every 1 min
    @Scheduled(fixedRate = 60000)
    public void updatePrices() {
        updateCoinPrices();
    }

    public Coin getByName(String name) {
        return coinRepo.findFirstByName(name.toUpperCase())
                .orElseThrow(() -> new NoSuchElementException("No crypto with name: " + name + " was found"));
    }

    public List<Coin> getAll() {
        return coinRepo.findAll();
    }



    public void updateCoinPrices() {
        String apiUrl = String.format(
                "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd",
                String.join(",", STANDARD_COIN_NAMES)
                );

        ResponseEntity<Map<String, Map<String, Double>>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        Map<String, Map<String, Double>> prices = response.getBody();

        if (prices != null) {
            prices.forEach((coinId, data) -> {
                String symbol = coinId.toUpperCase();
                Double price = data.get("usd");

                coinRepo.findFirstByName(symbol).ifPresentOrElse(
                        coin -> {
                            coin.setPrice(price);
                            coinRepo.save(coin);
                        },
                        () -> {
                            Coin newCoin = new Coin();
                            newCoin.setName(symbol);
                            newCoin.setPrice(price);
                            coinRepo.save(newCoin);
                        }
                );
            });
        }
    }

}
