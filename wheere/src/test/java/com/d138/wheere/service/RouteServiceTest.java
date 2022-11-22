package com.d138.wheere.service;

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

@SpringBootTest
@Transactional
public class RouteServiceTest {

    @Autowired
    private RouteService routeService;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void inquiryBusScheduleTest() {

        // Given
        LocalTime departureTime1 = LocalTime.now().plusMinutes(5);
        LocalTime departureTime3 = LocalTime.now().plusMinutes(80);
        LocalTime departureTime2 = LocalTime.now().minusMinutes(5);

        Bus bus1 = new Bus(100L, 1, BusState.FORWARD, "191", departureTime1);
        Bus bus2 = new Bus(200L, 2, BusState.FORWARD, "191", departureTime3);
        em.persist(bus1);
        em.persist(bus2);

        Station station1 = createStation(1L, "구미역");
        Station station2 = createStation(2L, "형곡동");
        Station station3 = createStation(3L, "금오공대");
        Station station4 = createStation(4L, "옥계");
        em.persist(station1);
        em.persist(station2);
        em.persist(station3);
        em.persist(station4);

        Route route1 = createRoute(1L, bus1, station1, 1, departureTime1);
        Route route2 = createRoute(2L, bus1, station2, 2, departureTime1.plusMinutes(10));
        Route route3 = createRoute(3L, bus1, station3, 3, departureTime1.plusMinutes(20));
        Route route4 = createRoute(4L, bus1, station4, 4, departureTime1.plusMinutes(30));
        em.persist(route1);
        em.persist(route2);
        em.persist(route3);
        em.persist(route4);

        Route route5 = createRoute(5L, bus2, station1, 1, departureTime3);
        Route route6 = createRoute(6L, bus2, station2, 2, departureTime3.plusMinutes(10));
        Route route7 = createRoute(7L, bus2, station3, 3, departureTime3.plusMinutes(20));
        Route route8 = createRoute(8L, bus2, station4, 4, departureTime3.plusMinutes(30));
        em.persist(route5);
        em.persist(route6);
        em.persist(route7);
        em.persist(route8);

        em.flush();
        em.clear();

        // When
        String busNum = bus1.getBusNumber();
        BusState direction = bus1.getDirection();
        List<Object[]> list = routeService.inquiryBusSchedule(busNum, direction, 2, 4);

        // Then

        for (Object[] objects : list) {
            for (int i = 0; i < objects.length; i++) {
                System.out.println("objects[" + i +"] = " + objects[i]);
            }
        }

        /*Object[] startObject = (Object[]) list.get(0);
        Object[] midObject = (Object[]) list.get(1);
        Object[] endObject = (Object[]) list.get(2);

        System.out.println("startObject[0] = " + startObject[0]);
        System.out.println("startObject[1] = " + startObject[1]);

        System.out.println("midObject[0] = " + midObject[0]);
        System.out.println("midObject[1] = " + midObject[1]);

        System.out.println("endObject[0] = " + endObject[0]);
        System.out.println("endObject[1] = " + endObject[1]);*/


    }

    private Station createStation(Long id, String name) {
        return new Station(id, name);
    }

    private Route createRoute(Long id, Bus bus, Station station, int stationSeq, LocalTime arrivalTime) {
        return new Route(id, bus, station, stationSeq, arrivalTime);
    }
}
