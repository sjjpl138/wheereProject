package com.d138.wheere.repository;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Route;
import com.d138.wheere.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class RouteRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    RouteRepository routeRepository;

    /*@Test
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

        Bus bus1 = new Bus(1L, null, 1, BusState.FORWARD, "191", LocalTime.of(8, 0, 0), 2, null);
        Bus bus2 = new Bus(2L, null, 2, BusState.FORWARD, "191", LocalTime.of(9, 0, 0), 2, null);
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

        List<Route> busRoute = routeRepository.findBusRoute(1L);

        for (Route route : busRoute) {
            System.out.println("route.getStation().getName( = " + route.getStation().getName());
        }

        System.out.println("=======================================");

        List<Route> busRouteByBus = routeRepository.findBusRouteByBusInfo("191", BusState.FORWARD);

        for (Route routeByBus : busRouteByBus) {
            System.out.println("routeByBus.getStation().getName() = " + routeByBus.getStation().getName());
        }
    }*/

    @Test
    public void findOneByBusAndStationNameTest() {

        // Given
        LocalTime departureTime = LocalTime.of(8, 0, 0);

        Bus bus = new Bus(1L, 1, BusState.FORWARD, "191", departureTime);
        em.persist(bus);

        Station station1 = createStation(1L, "구미역");
        Station station2 = createStation(2L, "형곡동");
        Station station3 = createStation(3L, "금오공대");
        Station station4 = createStation(4L, "옥계");
        em.persist(station1);
        em.persist(station2);
        em.persist(station3);
        em.persist(station4);

        Route route1 = createRoute(1L, bus, station1, 1, departureTime);
        Route route2 = createRoute(2L, bus, station2, 2, departureTime.plusMinutes(10));
        Route route3 = createRoute(3L, bus, station3, 3, departureTime.plusMinutes(20));
        Route route4 = createRoute(4L, bus, station4, 4, departureTime.plusMinutes(30));
        em.persist(route1);
        em.persist(route2);
        em.persist(route3);
        em.persist(route4);

        // When
        em.flush();
        em.clear();

        Route findRoute1 = routeRepository.findOneByBusAndStationName(bus.getId(), "구미역");
        Route findRoute2 = routeRepository.findOneByBusAndStationName(bus.getId(), "옥계");
        Route findRoute3 = routeRepository.findOneByBusAndStationName(bus.getId(), "금오공대");
        Route findRoute4 = routeRepository.findOneByBusAndStationName(bus.getId(), "형곡동");

        // Then
        assertThat(findRoute1.getStationSeq()).isEqualTo(1);
        assertThat(findRoute2.getStationSeq()).isEqualTo(4);
        assertThat(findRoute3.getStationSeq()).isEqualTo(3);
        assertThat(findRoute4.getStationSeq()).isEqualTo(2);

        assertThat(findRoute1.getArrivalTime()).isEqualTo(departureTime);
        assertThat(findRoute2.getArrivalTime()).isEqualTo(departureTime.plusMinutes(30));
        assertThat(findRoute3.getArrivalTime()).isEqualTo(departureTime.plusMinutes(20));
        assertThat(findRoute4.getArrivalTime()).isEqualTo(departureTime.plusMinutes(10));

    }

    private Station createStation(Long id, String name) {
        return new Station(id, name);
    }

    private Route createRoute(Long id, Bus bus, Station station, int stationSeq, LocalTime arrivalTime) {
        return new Route(id, bus, station, stationSeq, arrivalTime);
    }
}
