package ohonovskiy.ua.buycrypto.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {
    private final CoinService coinService;
    private final OrderService orderService;

    private final int SCHEDULE_SECONDS = 0;
    private final int SCHEDULE_MINUTES = 1;
    private final int SCHEDULE_HOURS = 0;

    private final int SCHEDULE_TIME = SCHEDULE_SECONDS * 1000
            + SCHEDULE_MINUTES * 1000 * 60
            + SCHEDULE_HOURS * 1000 * 60 * 60;

    public ScheduleService(CoinService coinService, OrderService orderService) {
        this.coinService = coinService;
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = SCHEDULE_TIME)
    public void scheduler() {
        //coinService.updateCoinPricesAndSaveChart();
        orderService.checkAndCompleteOrders();
    }
}
