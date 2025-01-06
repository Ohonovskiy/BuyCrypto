package ohonovskiy.ua.buycrypto.controller.test;

import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.service.CoinService;
import ohonovskiy.ua.buycrypto.service.TradingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
@Deprecated
public class TestController {
    private final CoinService coinService;
    private final TradingService tradingService;

    public TestController(CoinService coinService, TradingService tradingService) {
        this.coinService = coinService;
        this.tradingService = tradingService;
    }

    @GetMapping("/{coinName}")
    public String getPrice(@PathVariable String coinName) {
        Coin coin = coinService.findByName(coinName);

        return coin.toString();
    }

    @GetMapping("/buy/{coinName}")
    public String buy(@PathVariable String coinName) {
        tradingService.buyCoin(coinName, 1.);

        return "";
    }
}
