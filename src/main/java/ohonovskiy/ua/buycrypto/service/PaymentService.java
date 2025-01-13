package ohonovskiy.ua.buycrypto.service;

import ohonovskiy.ua.buycrypto.model.user.User;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final UserService userService;

    public PaymentService(UserService userService) {
        this.userService = userService;
    }

    public void addToBalance(Double balanceToAdd) {
        User currentUser = userService.getCurrentUser();

        currentUser.invest(balanceToAdd);

        userService.save(currentUser);
    }
}
