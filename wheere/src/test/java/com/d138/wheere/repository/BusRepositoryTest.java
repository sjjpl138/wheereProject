package com.d138.wheere.repository;

import com.d138.wheere.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class BusRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    BusRepository busRepository;

    @Autowired
    DriverRepository driverRepository;

    @Test
    public void 버스_배차_테스트() {

        // Given
        Station station1 = new Station(1L, "구미역");
        Station station2 = new Station(2L, "형곡동");
        Station station3 = new Station(3L, "금오공대");
        Station station4 = new Station(4L, "옥계");
        em.persist(station1);
        em.persist(station2);
        em.persist(station3);
        em.persist(station4);

        Bus bus1 = new Bus(1L, null, 1, BusState.FORWARD, "191", LocalTime.of(8, 0, 0), 2);
        Bus bus2 = new Bus(2L, null, 2, BusState.FORWARD, "191", LocalTime.of(9, 0, 0), 2);
        em.persist(bus1);
        em.persist(bus2);

        Route route1 = new Route(1L, bus1, station1, 1);
        Route route2 = new Route(2L, bus1, station2, 2);
        Route route3 = new Route(3L, bus1, station3, 3);
        Route route4 = new Route(4L, bus1, station4, 4);
        em.persist(route1);
        em.persist(route2);
        em.persist(route3);
        em.persist(route4);

        Route route5 = new Route(5L, bus2, station1, 1);
        Route route6 = new Route(6L, bus2, station2, 2);
        Route route7 = new Route(7L, bus2, station3, 3);
        Route route8 = new Route(8L, bus2, station4, 4);
        em.persist(route5);
        em.persist(route6);
        em.persist(route7);
        em.persist(route8);

        Driver driver = new Driver();
        driver.setId("driverr1");
        driver.setBus(null);
        driver.setName("홍길동");
        driver.setRatingScore(0.0);
        driver.setRatingCnt(0);
        em.persist(driver);

        // When
        em.flush();
        em.clear();

        Bus findBus = busRepository.findBusForDriver("191", BusState.FORWARD, LocalTime.of(8, 0, 0));

        Driver findDriver = driverRepository.findOne(driver.getId());

        // Then
        findDriver.changeBus(findBus);

        assertThat(findBus.getBusAllocationSeq()).isEqualTo(1);
        assertThat(findDriver.getName()).isEqualTo("홍길동");
        assertThat(findDriver.getBus().getId()).isEqualTo(1L);
        assertThat(findDriver.getBus().getBusNumber()).isEqualTo("191");
        assertThat(findDriver.getBus().getBusAllocationSeq()).isEqualTo(1);
    }
}
