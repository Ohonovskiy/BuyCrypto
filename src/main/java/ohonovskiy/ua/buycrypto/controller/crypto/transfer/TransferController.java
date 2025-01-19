package ohonovskiy.ua.buycrypto.controller.crypto.transfer;

import ohonovskiy.ua.buycrypto.DTO.transfer.CoinTransferRequest;
import ohonovskiy.ua.buycrypto.DTO.transfer.MoneyTransferRequest;
import ohonovskiy.ua.buycrypto.service.crypto.CoinService;
import ohonovskiy.ua.buycrypto.service.crypto.TransferService;
import ohonovskiy.ua.buycrypto.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;
    private final CoinService coinService;
    private final UserService userService;

    public TransferController(TransferService transferService, CoinService coinService, UserService userService) {
        this.transferService = transferService;
        this.coinService = coinService;
        this.userService = userService;
    }

    @GetMapping
    public String getTransfer(Model model) {
        model.addAttribute("userCoins", coinService.getAllForCurrentUser());
        model.addAttribute("balance", userService.getCurrentUser().getBalance());

        return "transfer/index";
    }

    @PostMapping("/money")
    public String postTransferMoney(@ModelAttribute MoneyTransferRequest moneyTransferRequest) {
        transferService.handleRequest(moneyTransferRequest);

        return "redirect:/user";
    }

    @PostMapping("/coin")
    public String postTransferCoin(@ModelAttribute CoinTransferRequest coinTransferRequest) {
        transferService.handleRequest(coinTransferRequest);

        return "redirect:/user";
    }
}
