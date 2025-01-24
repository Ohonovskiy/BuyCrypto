package ohonovskiy.ua.buycrypto.service.crypto;

import jakarta.transaction.Transactional;
import ohonovskiy.ua.buycrypto.DTO.crypto.transfer.CoinTransferRequest;
import ohonovskiy.ua.buycrypto.DTO.crypto.transfer.MoneyTransferRequest;
import ohonovskiy.ua.buycrypto.DTO.crypto.transfer.TransferRequest;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.model.user.User;
import ohonovskiy.ua.buycrypto.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    private final UserService userService;
    private final CoinService coinService;

    public TransferService(UserService userService, CoinService coinService) {
        this.userService = userService;
        this.coinService = coinService;
    }

    @Transactional
    public void handleRequest(TransferRequest transferRequest) {
        if(transferRequest instanceof MoneyTransferRequest) {
            transferMoneyTo(transferRequest.getAmount(), transferRequest.getUserId());
        } else if (transferRequest instanceof CoinTransferRequest) {
            transferCryptoTo(
                    transferRequest.getAmount(),
                    ((CoinTransferRequest) transferRequest).getCoinName(),
                    transferRequest.getUserId()
            );
        }
    }

    private void transferMoneyTo(Double amount, Long userId) {
        if(amount <= 0) throw new IllegalArgumentException("Amount must be higher than zero.");

        User toUser = userService.getById(userId);
        User currentUser = userService.getCurrentUser();

        currentUser.withdrawMoney(amount);
        toUser.invest(amount);

        userService.save(currentUser);
        userService.save(toUser);
    }

    private void transferCryptoTo(Double amount, String coinName, Long userId) {
        transferCryptoTo(amount, coinService.getByName(coinName), userId);
    }


    private void transferCryptoTo(Double amount, Coin coin, Long userId) {
        if(amount <= 0) throw new IllegalArgumentException("Amount must be higher than zero.");

        User currentUser = userService.getCurrentUser();
        User toUser = userService.getById(userId);

        coinService.removeCoinFromUser(currentUser, coin, amount);
        coinService.addCoinToUser(toUser, coin, amount);

        userService.save(currentUser);
        userService.save(toUser);
    }
}
