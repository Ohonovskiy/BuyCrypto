package ohonovskiy.ua.buycrypto.repository;

import ohonovskiy.ua.buycrypto.model.chart.Chart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartRepo extends JpaRepository<Chart, Long> {

}
