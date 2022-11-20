package com.d138.wheere.repository;

import com.d138.wheere.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class SeatRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private SeatRepository seatRepository;

    private Long mId = 0L;

    @Test
    public void findSeatByRouteAndDateTest() {

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

        LocalDate reservationDate = LocalDate.now().plusDays(1);

        Seat seat1 = new Seat(route1, 2, 1, reservationDate);
        Seat seat2 = new Seat(route2, 2, 4, reservationDate);
        Seat seat3 = new Seat(route3, 2, 5, reservationDate);
        Seat seat4 = new Seat(route4, 2, 6, reservationDate);
        seatRepository.save(seat1);
        seatRepository.save(seat2);
        seatRepository.save(seat3);
        seatRepository.save(seat4);

        em.flush();
        em.clear();

        // When
        Seat findSeat1 = seatRepository.findSeatByRouteAndDate(route1.getId(), reservationDate);
        Seat findSeat2 = seatRepository.findSeatByRouteAndDate(route2.getId(), reservationDate);
        Seat findSeat3 = seatRepository.findSeatByRouteAndDate(route3.getId(), reservationDate);
        Seat findSeat4 = seatRepository.findSeatByRouteAndDate(route4.getId(), reservationDate);

        // Then
        assertThat(findSeat1.getLeftSeatsNum()).isEqualTo(1);
        assertThat(findSeat2.getLeftSeatsNum()).isEqualTo(4);
        assertThat(findSeat3.getLeftSeatsNum()).isEqualTo(5);
        assertThat(findSeat4.getLeftSeatsNum()).isEqualTo(6);

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

    private int seq = 1;

    private Station createStation(Long id, String name) {
        return new Station(id, name);
    }

    private Route createRoute(Long id, Bus bus, Station station, int stationSeq, LocalTime arrivalTime) {
        return new Route(id, bus, station, stationSeq, arrivalTime);
    }
}
