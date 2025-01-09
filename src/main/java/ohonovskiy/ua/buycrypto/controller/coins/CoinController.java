package ohonovskiy.ua.buycrypto.controller.coins;

import ohonovskiy.ua.buycrypto.service.CoinService;
import ohonovskiy.ua.buycrypto.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coin")
public class CoinController {
    private final CoinService coinService;
    private final UserService userService;

    public CoinController(CoinService coinService, UserService userService) {
        this.coinService = coinService;
        this.userService = userService;
    }

    @GetMapping("/{coinName}")
    public String getCoin(@PathVariable String coinName, Model model) {
        model.addAttribute("coin", coinService.getByName(coinName));
        model.addAttribute("coinAmount", coinService.getCoinAmountForCurrentUser(coinName));
        model.addAttribute("user", userService.getCurrentUser());

        return "coin/index";
    }

}
