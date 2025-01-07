package ohonovskiy.ua.buycrypto.controller.index;

import ohonovskiy.ua.buycrypto.service.CoinService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class MainController {

    private final CoinService coinService;

    public MainController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("coins", coinService.getAll());

        return "index/index";
    }
}
