package ohonovskiy.ua.buycrypto.service;

import jakarta.transaction.Transactional;
import ohonovskiy.ua.buycrypto.DTO.OrderDTO;
import ohonovskiy.ua.buycrypto.enums.OrderType;
import ohonovskiy.ua.buycrypto.model.crypto.Coin;
import ohonovskiy.ua.buycrypto.model.crypto.Order;
import ohonovskiy.ua.buycrypto.model.crypto.UserCoin;
import ohonovskiy.ua.buycrypto.model.user.User;
import ohonovskiy.ua.buycrypto.repository.OrderRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final UserService userService;
    private final CoinService coinService;

    public OrderService(OrderRepo orderRepo, UserService userService, CoinService coinService) {
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.coinService = coinService;
    }

    public List<Order> getAllForCurrentUser() {
        return userService.getCurrentUser().getOrders();
    }


    @Transactional
    public void checkAndCompleteOrders() {
        List<Order> orders = orderRepo.findAll();

        for (Order o : orders) {
            User orderUser = o.getUser();
            Coin coin = o.getCoin();

            if (o.getOrderType().equals(OrderType.ORDER_SELL) && o.getPrice() <= coin.getPrice()) {
                orderUser.setBalance(orderUser.getBalance() + (o.getAmount() * o.getPrice())); // Add balance from the sell
                orderUser.removeOrder(o);
                userService.save(orderUser);
            } else if (o.getOrderType().equals(OrderType.ORDER_BUY) && o.getPrice() >= coin.getPrice()) {
                Optional<UserCoin> userCoin = orderUser.getUserCoins()
                        .stream()
                        .filter(uc -> uc.getCoin().equals(coin))
                        .findFirst();

                if (userCoin.isPresent()) {
                    userCoin.get().setAmount(userCoin.get().getAmount() + o.getAmount());
                } else {
                    UserCoin newUserCoin = new UserCoin();
                    newUserCoin.setUser(orderUser);
                    newUserCoin.setCoin(coin);
                    newUserCoin.setAmount(o.getAmount());
                    orderUser.getUserCoins().add(newUserCoin);
                }

                orderUser.removeOrder(o);
                userService.save(orderUser);
            }
        }
    }


    @Transactional
    public void placeOrder(Order order) {
        User currentUser = userService.getCurrentUser();
        order.setUser(currentUser);

        if (order.getOrderType().equals(OrderType.ORDER_BUY)) {
            reserveBalanceForOrder(order, currentUser);
        } else if (order.getOrderType().equals(OrderType.ORDER_SELL)) {
            reserveCoinsForOrder(order, currentUser);
        }

        currentUser.addOrder(order);
        userService.save(currentUser);
    }

    private void reserveCoinsForOrder(Order order, User user) {
        Optional<UserCoin> userCoin = user.getUserCoins()
                .stream()
                .filter(uc -> uc.getCoin().equals(order.getCoin()))
                .findFirst();

        if (userCoin.isEmpty() || userCoin.get().getAmount() < order.getAmount()) {
            throw new RuntimeException("Not enough coins to place the order!");
        }

        userCoin.get().setAmount(userCoin.get().getAmount() - order.getAmount());
        if (userCoin.get().getAmount() == 0) {
            user.getUserCoins().remove(userCoin.get());
        }
    }

    private void reserveBalanceForOrder(Order order, User user) {
        double totalCost = order.getPrice() * order.getAmount();
        if (user.getBalance() < totalCost) {
            throw new RuntimeException("Not enough balance to place the order!");
        }
        user.setBalance(user.getBalance() - totalCost);
    }

    public Order OrderDTOToOrder(OrderDTO orderDTO) {
        return Order.builder()
                .orderType(orderDTO.getOrderType())
                .amount(orderDTO.getAmount())
                .coin(coinService.getByName(orderDTO.getCoinName()))
                .price(orderDTO.getPrice())
                .build();
    }

    // TODO return coins\balance
    @Transactional
    public void removeOrder(Order order) {
        User currentUser = userService.getCurrentUser();

        currentUser.removeOrder(order);

        userService.save(currentUser);
    }
}
