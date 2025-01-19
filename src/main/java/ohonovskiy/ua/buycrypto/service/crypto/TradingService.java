package ohonovskiy.ua.buycrypto.service.crypto;

import jakarta.transaction.Transactional;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.model.user.User;
import ohonovskiy.ua.buycrypto.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class TradingService {
    final CoinService coinService;
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

        User currentUser = userService.getByUsername(user.getUsername());

        Coin coinToBuy = coinService.getByName(coinName);

        currentUser.removeFromBalance(coinToBuy.getPrice() * amount);

        coinService.addCoinToUser(currentUser, coinToBuy, amount);

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

        User currentUser = userService.getByUsername(user.getUsername());

        Coin coinToSell = coinService.getByName(coinName);
        if (coinToSell == null) {
            throw new IllegalArgumentException("Coin not found: " + coinName);
        }

        coinService.removeCoinFromUser(currentUser, coinToSell, amount);

        double totalCost = coinToSell.getPrice() * amount;

        currentUser.setBalance(currentUser.getBalance() + totalCost);

        userService.save(currentUser);
    }

}
