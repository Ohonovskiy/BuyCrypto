package ohonovskiy.ua.buycrypto.controller.order;

import ohonovskiy.ua.buycrypto.DTO.OrderDTO;
import ohonovskiy.ua.buycrypto.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String postOrder(@ModelAttribute OrderDTO orderDTO) {

        orderService.placeOrder(orderService.OrderDTOToOrder(orderDTO));
        return "redirect:/order";
    }

    @PostMapping(value = "/cancel", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String postCancel(@RequestParam Long orderId) {
        orderService.cancelOrder(orderId);

        return "redirect:/order";
    }


}
