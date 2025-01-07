package ohonovskiy.ua.buycrypto.controller.coins;

import ohonovskiy.ua.buycrypto.service.CoinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coin")
public class CoinController {
    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping("/{coinName}")
    public String getCoin(@PathVariable String coinName, Model model) {
        model.addAttribute("coin", coinService.getByName(coinName));

        return "coin/index";
    }

}
