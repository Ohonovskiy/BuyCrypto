package ohonovskiy.ua.buycrypto.controller.user;

import ohonovskiy.ua.buycrypto.service.CoinService;
import ohonovskiy.ua.buycrypto.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final CoinService coinService;

    public UserController(UserService userService, CoinService coinService) {
        this.userService = userService;
        this.coinService = coinService;
    }

    @GetMapping()
    public String getProfile(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("roi", coinService.calculateROI());
        return "user/index";
    }
}
