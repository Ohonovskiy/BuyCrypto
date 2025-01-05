package ohonovskiy.ua.buycrypto.controller.test;

import ohonovskiy.ua.buycrypto.service.CoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
@Deprecated
public class TestController {
    private final CoinService coinService;

    public TestController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping("/{coinName}")
    public String getPrice(@PathVariable String coinName) {
        return coinService.getUpdatedPrice(coinName).toString();
    }
}
