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

        User currentUser = userService.getCurrentUser();

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

}
