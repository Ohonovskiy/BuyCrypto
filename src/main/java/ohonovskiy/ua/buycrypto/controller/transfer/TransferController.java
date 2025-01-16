package ohonovskiy.ua.buycrypto.controller.transfer;

import ohonovskiy.ua.buycrypto.DTO.transfer.TransferRequest;
import ohonovskiy.ua.buycrypto.service.CoinService;
import ohonovskiy.ua.buycrypto.service.TransferService;
import ohonovskiy.ua.buycrypto.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public String postTransfer(@ModelAttribute TransferRequest transferRequest) {
        transferService.handleRequest(transferRequest);

        return "redirect:/user";
    }
}
