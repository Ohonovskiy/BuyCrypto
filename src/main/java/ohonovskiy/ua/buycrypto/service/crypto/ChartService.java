package ohonovskiy.ua.buycrypto.service.crypto;

import ohonovskiy.ua.buycrypto.model.crypto.chart.Chart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartService {
    private final CoinService coinService;

    public ChartService(CoinService coinService) {
        this.coinService = coinService;
    }

    public List<Chart> getPriceHistory(String coinName) {
        return coinService.getByName(coinName).getCharts();
    }

}
