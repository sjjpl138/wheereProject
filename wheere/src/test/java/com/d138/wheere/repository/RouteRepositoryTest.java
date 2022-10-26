package com.d138.wheere.repository;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Route;
import com.d138.wheere.domain.Station;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@Transactional
public class RouteRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    RouteRepository routeRepository;

    @Test
    public void 버스노선_출력_테스트() {

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

        // When
        em.flush();
        em.clear();

        // Then

        /*List<Route> busRoute = routeRepository.findBusRoute(1L);

        for (Route route : busRoute) {
            System.out.println("route.getStation().getName( = " + route.getStation().getName());
        }*/

        List<Route> busRouteByBus = routeRepository.findBusRouteByBusInfo("191", BusState.FORWARD);

        for (Route routeByBus : busRouteByBus) {
            System.out.println("routeByBus.getStation().getName() = " + routeByBus.getStation().getName());
        }
    }
}
