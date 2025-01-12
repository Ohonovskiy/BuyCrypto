package ohonovskiy.ua.buycrypto.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private final CoinService coinService;
    private final OrderService orderService;

    public ScheduleService(CoinService coinService, OrderService orderService) {
        this.coinService = coinService;
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 60_000)
    public void scheduler() {
        coinService.updateCoinPricesAndSaveChart();
        orderService.checkAndCompleteOrders();
    }
}
