package ohonovskiy.ua.buycrypto.service;

import ohonovskiy.ua.buycrypto.model.chart.Chart;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.model.crypto.UserCoin;
import ohonovskiy.ua.buycrypto.model.user.User;
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
import java.util.Optional;

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
    private final UserService userService;

    public CoinService(CoinRepo coinRepo, RestTemplate restTemplate, UserService userService) {
        this.coinRepo = coinRepo;
        this.restTemplate = restTemplate;
        this.userService = userService;
    }

    // execute every 1 min
    @Scheduled(fixedRate = 60_000)
    public void updatePrices() {
        updateCoinPricesAndSaveChart();
    }


    // calculating ROI using owned coin's prices and user balance
    public Double calculateROI() {
        User currentUser = userService.getCurrentUser();

        Double coinValue = currentUser.getUserCoins()
                .stream()
                .mapToDouble(uc -> uc.getCoin().getPrice() * uc.getAmount())
                .sum();

        return currentUser.getBalance() + coinValue - currentUser.getInvested();
    }

    public Double getCoinAmountForCurrentUser(String coinName) {

        Optional<UserCoin> userCoin = userService.getCurrentUser().getUserCoins()
                .stream()
                .filter(c -> c.getCoin().getName().equals(coinName))
                .findFirst();

        return userCoin.isPresent() ? userCoin.get().getAmount() : 0.;
    }

    public Coin getByName(String name) {
        return coinRepo.findFirstByName(name.toUpperCase())
                .orElseThrow(() -> new NoSuchElementException("No crypto with name: " + name + " was found"));
    }

    public List<Coin> getAll() {
        return coinRepo.findAll();
    }



    public void updateCoinPricesAndSaveChart() {

        // Getting coin info
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

                Chart chart = new Chart();

                // coin already exists ? update price and save chart : create new coin and save chart
                coinRepo.findFirstByName(symbol).ifPresentOrElse(
                        coin -> {
                            chart.setCoin(coin);
                            chart.setPrice(price);

                            coin.setPrice(price);
                            coin.addChart(chart);

                            coinRepo.save(coin);
                        },
                        () -> {
                            Coin newCoin = new Coin();
                            chart.setPrice(price);
                            chart.setCoin(newCoin);

                            newCoin.setName(symbol);
                            newCoin.setPrice(price);
                            newCoin.addChart(chart);

                            coinRepo.save(newCoin);
                        }
                );
            });
        }
    }

}
