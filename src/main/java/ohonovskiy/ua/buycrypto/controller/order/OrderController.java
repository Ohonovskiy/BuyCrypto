package ohonovskiy.ua.buycrypto.controller.order;

import ohonovskiy.ua.buycrypto.DTO.OrderDTO;
import ohonovskiy.ua.buycrypto.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getOrder(Model model) {
        model.addAttribute("orders", orderService.getAllForCurrentUser());
        return "order/index";
    }

    @PostMapping
    public String postOrder(@RequestBody OrderDTO orderDTO) {
        orderService.placeOrder(orderService.OrderDTOToOrder(orderDTO));
        return "redirect:/order";
    }


}
