package com.d138.wheere.service;

import com.d138.wheere.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class SeatServiceTest {

    @Autowired
    private SeatService seatService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ReservationService reservationService;

    private Long mId = 0L;


    @Test
    public void inquiryMinLeftSeatNumTest() {

        // Given
        String memberId1 = createMember("홍길동");
        String memberId2 = createMember("철수");
        String memberId3 = createMember("유리");
        String memberId4 = createMember("짱구");
        String memberId5 = createMember("맹구");

        LocalTime departureTime1 = LocalTime.now().plusMinutes(5);
        LocalTime departureTime2 = LocalTime.now().minusMinutes(5);

        Bus bus1 = new Bus(1L, 1, BusState.FORWARD, "191", departureTime1);
        Bus bus2 = new Bus(2L, 2, BusState.FORWARD, "191", departureTime1);
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

        Route route5 = createRoute(5L, bus2, station1, 1, departureTime2);
        Route route6 = createRoute(6L, bus2, station2, 2, departureTime2.plusMinutes(1));
        Route route7 = createRoute(7L, bus2, station3, 3, departureTime2.plusMinutes(1));
        Route route8 = createRoute(8L, bus2, station4, 4, departureTime2.plusMinutes(1));
        em.persist(route5);
        em.persist(route6);
        em.persist(route7);
        em.persist(route8);

        // When
        LocalDate reservationDate = LocalDate.now().plusDays(1);
        Long findReservationId1 = reservationService.saveReservation(memberId1, bus1.getId(), 1, 3, reservationDate);
        Long findReservationId2 = reservationService.saveReservation(memberId2, bus1.getId(), 1, 2, reservationDate);
        /*Long findReservationId3 = reservationService.saveReservation(memberId3, bus1.getId(), 3, 4, reservationDate);
        Long findReservationId4 = reservationService.saveReservation(memberId4, bus1.getId(), 2, 4, reservationDate);*/

        reservationService.saveReservation(memberId1, bus1.getId(), 1, 3, reservationDate.plusDays(3));

        em.flush();
        em.clear();

        // Then
        int i = seatService.inquiryMinLeftSeatNum(bus1.getBusNumber(), bus1.getDirection(), reservationDate, 1, 3);
        int i2 = seatService.inquiryMinLeftSeatNum(bus1.getBusNumber(), bus1.getDirection(), reservationDate, 3, 4);
        int i3 = seatService.inquiryMinLeftSeatNum(bus1.getBusNumber(), bus1.getDirection(), reservationDate, 2, 4);

        System.out.println("i = " + i);
        System.out.println("i2 = " + i2);
        System.out.println("i3 = " + i3);

        assertThat(i).isEqualTo(0);

    }

    private String createMember(String name) {
        Member member = new Member();
        member.setId((mId++).toString());
        member.setName(name);
        member.setBirthDate(LocalDate.of(1999, 03, 02));
        member.setPhoneNumber("010-1111-1111");
        member.setSex("female");

        em.persist(member);
        return member.getId();
    }

    private Station createStation(Long id, String name) {
        return new Station(id, name);
    }

    private Route createRoute(Long id, Bus bus, Station station, int stationSeq, LocalTime arrivalTime) {
        return new Route(id, bus, station, stationSeq, arrivalTime);
    }
}
