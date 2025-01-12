package ohonovskiy.ua.buycrypto.controller.order;

import ohonovskiy.ua.buycrypto.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    //TODO
    @PostMapping
    public String postOrder() {
        return "";
    }


}
