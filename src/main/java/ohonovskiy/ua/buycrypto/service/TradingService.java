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

        Optional<UserCoin> existingUserCoin = currentUser.getUserCoins()
                .stream()
                .filter(userCoin -> userCoin.getCoin().equals(coinToBuy))
                .findFirst();

        if (existingUserCoin.isPresent()) {
            UserCoin userCoin = existingUserCoin.get();
            userCoin.setAmount(userCoin.getAmount() + amount);
        } else {
            UserCoin newUserCoin = new UserCoin();
            newUserCoin.setUser(currentUser);
            newUserCoin.setCoin(coinToBuy);
            newUserCoin.setAmount(amount);
            currentUser.getUserCoins().add(newUserCoin);
        }

        currentUser.setBalance(currentUser.getBalance() - totalCost);

        userService.save(currentUser);
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

        Optional<UserCoin> existingUserCoin = currentUser.getUserCoins()
                .stream()
                .filter(userCoin -> userCoin.getCoin().equals(coinToSell))
                .findFirst();

        UserCoin userCoin = existingUserCoin.orElseThrow(() ->
                new IllegalArgumentException("User doesn't have such coin: " + coinName));

        if (userCoin.getAmount() < amount) {
            throw new IllegalArgumentException("Insufficient coin amount to sell");
        }

        double totalCost = coinToSell.getPrice() * amount;

        userCoin.setAmount(userCoin.getAmount() - amount);

        if (userCoin.getAmount() == 0) {
            currentUser.getUserCoins().remove(userCoin);
        }

        currentUser.setBalance(currentUser.getBalance() + totalCost);

        userService.save(currentUser);
    }

}
