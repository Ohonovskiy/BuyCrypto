package ohonovskiy.ua.buycrypto.controller.coins;

import ohonovskiy.ua.buycrypto.service.ChartService;
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
    private final ChartService chartService;

    public CoinController(CoinService coinService, UserService userService, ChartService chartService) {
        this.coinService = coinService;
        this.userService = userService;
        this.chartService = chartService;
    }

    @GetMapping("/{coinName}")
    public String getCoin(@PathVariable String coinName, Model model) {
        model.addAttribute("coin", coinService.getDTOByName(coinName));
        model.addAttribute("coinAmount", coinService.getCoinAmountForCurrentUser(coinName));
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("charts", chartService.getPriceHistory(coinName));

        return "coin/index";
    }

}
