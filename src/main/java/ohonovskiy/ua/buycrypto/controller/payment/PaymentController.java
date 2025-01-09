package ohonovskiy.ua.buycrypto.controller.payment;

import ohonovskiy.ua.buycrypto.service.PaymentService;
import ohonovskiy.ua.buycrypto.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;

    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @GetMapping
    public String getPayment(Model model) {
        model.addAttribute("user", userService.getCurrentUser());

        return "payment/index";
    }

    @PostMapping
    public String postPayment(@RequestParam Double amount) {
        paymentService.addToBalance(amount);

        return "redirect:/user";
    }
}
