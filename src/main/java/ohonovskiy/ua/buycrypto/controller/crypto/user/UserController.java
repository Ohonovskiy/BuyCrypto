package ohonovskiy.ua.buycrypto.controller.crypto.user;

import ohonovskiy.ua.buycrypto.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getProfile(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("roi", userService.calculateROI());
        return "user/index";
    }
}
