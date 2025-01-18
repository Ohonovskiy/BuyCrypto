package ohonovskiy.ua.buycrypto.service;

import jakarta.transaction.Transactional;
import ohonovskiy.ua.buycrypto.DTO.CoinDTO;
import ohonovskiy.ua.buycrypto.model.chart.Chart;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.model.crypto.UserCoin;
import ohonovskiy.ua.buycrypto.model.user.User;
import ohonovskiy.ua.buycrypto.repository.CoinRepo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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


    public Double getCoinAmountForCurrentUser(String coinName) {

        Optional<UserCoin> userCoin = userService.getCurrentUser().getUserCoins()
                .stream()
                .filter(c -> c.getCoin().getName().equals(coinName))
                .findFirst();

        return userCoin.isPresent() ? userCoin.get().getAmount() : 0.;
    }

    @Transactional
    public Coin getByName(String name) {
        return coinRepo.findFirstByName(name.toUpperCase())
                .orElseThrow(() -> new NoSuchElementException("No crypto with name: " + name + " was found"));
    }

    public CoinDTO getDTOByName(String name) {
        return coinToCoinDTO(coinRepo.findFirstByName(name.toUpperCase())
                .orElseThrow(() -> new NoSuchElementException("No crypto with name: " + name + " was found")));
    }

    public CoinDTO coinToCoinDTO(Coin coin) {
        return CoinDTO.builder()
                .description(coin.getDescription())
                .imgUrl(coin.getImgUrl())
                .price(coin.getPrice())
                .name(coin.getName())
                .build();
    }


    @Transactional
    public List<Coin> getAll() {
        return coinRepo.findAll();
    }



    @Transactional
    protected void updateCoinPricesAndSaveChart() {

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

    public void addCoinToUser(User user, Coin coin, Double amount) {
        Optional<UserCoin> existingUserCoin = user.getUserCoins()
                .stream()
                .filter(userCoin -> userCoin.getCoin().equals(coin))
                .findFirst();

        if (existingUserCoin.isPresent()) {
            UserCoin userCoin = existingUserCoin.get();
            userCoin.setAmount(userCoin.getAmount() + amount);
        } else {
            UserCoin newUserCoin = new UserCoin();
            newUserCoin.setUser(user);
            newUserCoin.setCoin(coin);
            newUserCoin.setAmount(amount);
            user.getUserCoins().add(newUserCoin);
        }
    }

    public void removeCoinFromUser(User user, Coin coin, Double amount) {
        Optional<UserCoin> existingUserCoin = user.getUserCoins()
                .stream()
                .filter(userCoin -> userCoin.getCoin().equals(coin))
                .findFirst();

        UserCoin userCoin = existingUserCoin.orElseThrow(() ->
                new IllegalArgumentException("User doesn't have such coin: " + coin.getName()));

        if (userCoin.getAmount() < amount) {
            throw new IllegalArgumentException("Not enough coins");
        }

        userCoin.setAmount(userCoin.getAmount() - amount);

        if (userCoin.getAmount() == 0) {
            user.getUserCoins().remove(userCoin);
        }
    }

    public List<UserCoin> getAllForCurrentUser() {
        return userService.getCurrentUser().getUserCoins();
    }


}
