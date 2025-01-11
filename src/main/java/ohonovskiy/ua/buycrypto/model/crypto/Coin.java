package ohonovskiy.ua.buycrypto.model.crypto;

import jakarta.persistence.*;
import lombok.*;
import ohonovskiy.ua.buycrypto.model.SimpleEntityModel;
import ohonovskiy.ua.buycrypto.model.chart.Chart;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@ToString
public class Coin extends SimpleEntityModel {

    private Double price;

    @Column(unique = true)
    private String name;

    private String imgUrl;

    @OneToMany(mappedBy = "coin", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Chart> charts = new ArrayList<>();

    public void addChart(Chart chart) {
        if(this.charts == null) this.charts = new ArrayList<>();
        chart.setCoin(this);
        charts.add(chart);
    }

    public void removeChart(Chart chart) {
        chart.setCoin(null);
        charts.remove(chart);
    }
}
