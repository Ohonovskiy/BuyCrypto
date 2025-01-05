package ohonovskiy.ua.buycrypto.controller.auth;

import ohonovskiy.ua.buycrypto.DTO.UsernamePasswordRequest;
import ohonovskiy.ua.buycrypto.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String getSignUp(){
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String postSignUp(@RequestBody UsernamePasswordRequest request) {
        userService.register(request);
        return "redirect:/sign-in";
    }

    @GetMapping("/sign-in")
    public String getSignIn(){
        return "auth/sign-in";
    }

    @GetMapping("/sign-out")
    private String getSignOut() {
        return "auth/sign-out";
    }

}
