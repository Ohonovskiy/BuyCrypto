package ohonovskiy.ua.buycrypto.service;

import jakarta.transaction.Transactional;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.model.crypto.UserCoin;
import ohonovskiy.ua.buycrypto.model.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TradingService {
    private final CoinService coinService;
    private final UserService userService;

    public TradingService(CoinService coinService, UserService userService) {
        this.coinService = coinService;
        this.userService = userService;
    }

    @Transactional
    public void buyCoin(String coinName, Double amount) {
        buyCoin(coinName, amount, userService.getCurrentUser());
    }

    @Transactional
    public void buyCoin(String coinName, Double amount, User user) {

        User currentUser = userService.getByUsername(user.getUsername())
                .orElseThrow(IllegalArgumentException::new);

        Coin coinToBuy = coinService.getByName(coinName);

        double totalCost = coinToBuy.getPrice() * amount;

        if (currentUser.getBalance() < totalCost) {
            throw new RuntimeException("Not enough money!");
        }

        addCoinToUser(currentUser, coinToBuy, amount);

        currentUser.setBalance(currentUser.getBalance() - totalCost);

        userService.save(currentUser);
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

    @Transactional
    public void sellCoin(String coinName, Double amount) {
        sellCoin(coinName, amount, userService.getCurrentUser());
    }

    @Transactional
    public void sellCoin(String coinName, Double amount, User user) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        User currentUser = userService.getByUsername(user.getUsername())
                .orElseThrow(IllegalArgumentException::new);

        Coin coinToSell = coinService.getByName(coinName);
        if (coinToSell == null) {
            throw new IllegalArgumentException("Coin not found: " + coinName);
        }

        removeCoinFromUser(currentUser, coinToSell, amount);

        double totalCost = coinToSell.getPrice() * amount;

        currentUser.setBalance(currentUser.getBalance() + totalCost);

        userService.save(currentUser);
    }

    public void removeCoinFromUser(User user, Coin coin, Double amount) {
        Optional<UserCoin> existingUserCoin = user.getUserCoins()
                .stream()
                .filter(userCoin -> userCoin.getCoin().equals(coin))
                .findFirst();

        UserCoin userCoin = existingUserCoin.orElseThrow(() ->
                new IllegalArgumentException("User doesn't have such coin: " + coin.getName()));

        if (userCoin.getAmount() < amount) {
            throw new IllegalArgumentException("Not enough coins to sell");
        }

        userCoin.setAmount(userCoin.getAmount() - amount);

        if (userCoin.getAmount() == 0) {
            user.getUserCoins().remove(userCoin);
        }
    }


}
