package ohonovskiy.ua.buycrypto.service;

import jakarta.transaction.Transactional;
import ohonovskiy.ua.buycrypto.enums.OrderType;
import ohonovskiy.ua.buycrypto.model.crypto.Order;
import ohonovskiy.ua.buycrypto.model.user.User;
import ohonovskiy.ua.buycrypto.repository.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final UserService userService;
    private final TradingService tradingService;

    public OrderService(OrderRepo orderRepo, UserService userService, TradingService tradingService) {
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.tradingService = tradingService;
    }


    @Transactional
    protected void checkOrders() {
        List<Order> orders = orderRepo.findAll();

        for (Order o : orders) {
            if (o.getOrderType().equals(OrderType.ORDER_BUY) && // order to buy
                    o.getPrice() <= o.getCoin().getPrice()) {
                tradingService.buyCoin(o.getCoin().getName(), o.getAmount(), o.getUser());
            } else if (
                    o.getOrderType().equals(OrderType.ORDER_SELL) && // order to sell
                    o.getPrice() >= o.getCoin().getPrice()) {
                tradingService.sellCoin(o.getCoin().getName(), o.getAmount(), o.getUser());
            }
        }
    }

    @Transactional
    public void placeOrder(Order order) {
        User currentUser = userService.getCurrentUser();

        currentUser.addOrder(order);
    }

    @Transactional
    public void cancelOder(Order order) {
        User currentUser = userService.getCurrentUser();

        currentUser.cancelOrder(order);
    }
}
