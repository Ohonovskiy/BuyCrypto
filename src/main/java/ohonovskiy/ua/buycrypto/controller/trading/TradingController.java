package ohonovskiy.ua.buycrypto.controller.trading;

import ohonovskiy.ua.buycrypto.service.TradingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/trading")
public class TradingController {

    private final TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @PostMapping("/buy")
    public String postBuy(@RequestParam String coinName, @RequestParam Double amount) {
        tradingService.buyCoin(coinName, amount);
        return "redirect:/coin/" + coinName;
    }

    @PostMapping("/sell")
    public String postSell(@RequestParam String coinName, @RequestParam Double amount) {
        tradingService.sellCoin(coinName, amount);
        return "redirect:/coin/" + coinName;
    }
}
