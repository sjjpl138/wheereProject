package com.d138.wheere.service;

import com.d138.wheere.domain.*;
import com.d138.wheere.exception.NotEnoughSeatsException;
import com.d138.wheere.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ReservationServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ReservationService reservationService;

    @Autowired
    MemberService memberService;

    @Autowired
    BusService busService;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    BusRepository busRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    SeatRepository seatRepository;

    private Long mId = 0L;
    private Long bId = 0L;

    @Test
    public void 예약하기_With_예외발생_테스트_With_예약승낙() {

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
        Long findReservationId3 = reservationService.saveReservation(memberId3, bus1.getId(), 3, 4, reservationDate);
        Long findReservationId4 = reservationService.saveReservation(memberId4, bus1.getId(), 2, 4, reservationDate);

        reservationService.saveReservation(memberId1, bus1.getId(), 1, 3, reservationDate.plusDays(3));

        em.flush();
        em.clear();

        // Then
        Seat findSeat1 = seatRepository.findSeatByRouteAndDate(route1.getId(), reservationDate);
        Seat findSeat2 = seatRepository.findSeatByRouteAndDate(route2.getId(), reservationDate);
        Seat findSeat3 = seatRepository.findSeatByRouteAndDate(route3.getId(), reservationDate);
        Seat findSeat4 = seatRepository.findSeatByRouteAndDate(route4.getId(), reservationDate);

        // 예약시 상태는 대기 (WAITING) 상태여야 한다.
        Reservation findReservation1 = reservationRepository.findOne(findReservationId1);
        Reservation findReservation2 = reservationRepository.findOne(findReservationId2);
        Reservation findReservation3 = reservationRepository.findOne(findReservationId3);
        Reservation findReservation4 = reservationRepository.findOne(findReservationId4);
        assertThat(findReservation1.getReservationState()).isEqualTo(ReservationState.WAITING);
        assertThat(findReservation2.getReservationState()).isEqualTo(ReservationState.WAITING);
        assertThat(findReservation3.getReservationState()).isEqualTo(ReservationState.WAITING);
        assertThat(findReservation4.getReservationState()).isEqualTo(ReservationState.WAITING);


        assertThat(findSeat1.getLeftSeatsNum()).isEqualTo(0);
        assertThat(findSeat2.getLeftSeatsNum()).isEqualTo(0);
        assertThat(findSeat3.getLeftSeatsNum()).isEqualTo(0);
        assertThat(findSeat4.getLeftSeatsNum()).isEqualTo(2);

        assertThat(findReservation1.getStartPoint()).isEqualTo("구미역");
        assertThat(findReservation1.getEndPoint()).isEqualTo("금오공대");
        assertThat(findReservation2.getStartPoint()).isEqualTo("구미역");
        assertThat(findReservation2.getEndPoint()).isEqualTo("형곡동");
        assertThat(findReservation3.getStartPoint()).isEqualTo("금오공대");
        assertThat(findReservation3.getEndPoint()).isEqualTo("옥계");
        assertThat(findReservation4.getStartPoint()).isEqualTo("형곡동");
        assertThat(findReservation4.getEndPoint()).isEqualTo("옥계");

//          예외 발생 확인
        // 예약 가능한 남은 좌석이 없는 경우
        assertThrows(NotEnoughSeatsException.class, () ->
                reservationService.saveReservation(memberId5, bus1.getId(), 1, 2, reservationDate));

        // 이미 예약이 존재하는 경우
        assertThrows(IllegalStateException.class, () ->
                reservationService.saveReservation(memberId1, bus1.getId(), 3, 4, reservationDate));

        // 버스가 이미 지나간 경우 (시간)
        assertThrows(IllegalStateException.class, () ->
                reservationService.saveReservation(memberId5, bus2.getId(), 1, 4, reservationDate));

        // 버스가 이미 지나간 경우 (날짜)
        assertThrows(IllegalStateException.class, () ->
                reservationService.saveReservation(memberId5, bus1.getId(), 1, 4, LocalDate.now().minusDays(1)));

        reservationService.permitReservation(findReservationId1);

        em.flush();
        em.clear();
        Reservation findReservation15 = reservationRepository.findOne(findReservationId1);
        assertThat(findReservation15.getReservationState()).isEqualTo(ReservationState.RESERVED);

        reservationService.completeReservation(findReservationId1);
        em.flush();
        em.clear();
        Reservation findReservation20 = reservationRepository.findOne(findReservationId1);
        assertThat(findReservation20.getReservationState()).isEqualTo(ReservationState.COMP);
    }

    @Test
    public void 예약취소_With_예외발생_테스트() {
        // Given
        String memberId1 = createMember("홍길동");

        LocalTime departureTime1 = LocalTime.now().plusMinutes(5);

        Bus bus1 = new Bus(1L, 1, BusState.FORWARD, "191", departureTime1);
        em.persist(bus1);

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

        // When
        LocalDate reservationDate = LocalDate.now().plusDays(1);
        Long findReservationId1 = reservationService.saveReservation(memberId1, bus1.getId(), 1, 3, reservationDate);

        // Then
        Seat findSeat1 = seatRepository.findSeatByRouteAndDate(route1.getId(), reservationDate);
        Seat findSeat2 = seatRepository.findSeatByRouteAndDate(route2.getId(), reservationDate);
        Seat findSeat3 = seatRepository.findSeatByRouteAndDate(route3.getId(), reservationDate);

        // 예약 취소
        assertThat(findSeat1.getLeftSeatsNum()).isEqualTo(1);
        assertThat(findSeat2.getLeftSeatsNum()).isEqualTo(1);
        assertThat(findSeat3.getLeftSeatsNum()).isEqualTo(2);

        System.out.println("============= before ===================");
        reservationService.cancelReservation(findReservationId1);
        System.out.println("============= after ===================");

        em.flush();
        em.clear();

        Seat findSeat4 = seatRepository.findSeatByRouteAndDate(route1.getId(), reservationDate);
        Seat findSeat5 = seatRepository.findSeatByRouteAndDate(route2.getId(), reservationDate);
        Seat findSeat6 = seatRepository.findSeatByRouteAndDate(route3.getId(), reservationDate);

        assertThat(findSeat4.getLeftSeatsNum()).isEqualTo(2);
        assertThat(findSeat5.getLeftSeatsNum()).isEqualTo(2);
        assertThat(findSeat6.getLeftSeatsNum()).isEqualTo(2);

        Reservation findReservation = reservationRepository.findOne(findReservationId1);
        assertThat(findReservation.getReservationState()).isEqualTo(ReservationState.CANCEL);

        assertThrows(IllegalStateException.class, () ->
                reservationService.cancelReservation(findReservationId1));

        assertThrows(IllegalStateException.class, () ->
                reservationService.permitReservation(findReservationId1));

        assertThrows(IllegalStateException.class, () ->
                reservationService.completeReservation(findReservationId1));

    }

    @Test
    public void 예약거절_With_예외발생_테스트() {
        // Given
        String memberId1 = createMember("홍길동");

        LocalTime departureTime1 = LocalTime.now().plusMinutes(5);

        Bus bus1 = new Bus(1L, 1, BusState.FORWARD, "191", departureTime1);
        em.persist(bus1);

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

        // When
        LocalDate reservationDate = LocalDate.now().plusDays(1);
        Long findReservationId1 = reservationService.saveReservation(memberId1, bus1.getId(), 1, 3, reservationDate);

        // Then
        Seat findSeat1 = seatRepository.findSeatByRouteAndDate(route1.getId(), reservationDate);
        Seat findSeat2 = seatRepository.findSeatByRouteAndDate(route2.getId(), reservationDate);
        Seat findSeat3 = seatRepository.findSeatByRouteAndDate(route3.getId(), reservationDate);

        // 예약 취소
        assertThat(findSeat1.getLeftSeatsNum()).isEqualTo(1);
        assertThat(findSeat2.getLeftSeatsNum()).isEqualTo(1);
        assertThat(findSeat3.getLeftSeatsNum()).isEqualTo(2);

        System.out.println("============= before ===================");
        reservationService.rejectReservation(findReservationId1);
        System.out.println("============= after ===================");

        em.flush();
        em.clear();

        Seat findSeat4 = seatRepository.findSeatByRouteAndDate(route1.getId(), reservationDate);
        Seat findSeat5 = seatRepository.findSeatByRouteAndDate(route2.getId(), reservationDate);
        Seat findSeat6 = seatRepository.findSeatByRouteAndDate(route3.getId(), reservationDate);

        assertThat(findSeat4.getLeftSeatsNum()).isEqualTo(2);
        assertThat(findSeat5.getLeftSeatsNum()).isEqualTo(2);
        assertThat(findSeat6.getLeftSeatsNum()).isEqualTo(2);

        Reservation findReservation = reservationRepository.findOne(findReservationId1);
        assertThat(findReservation.getReservationState()).isEqualTo(ReservationState.REFUSED);

        assertThrows(IllegalStateException.class, () ->
                reservationService.cancelReservation(findReservationId1));

        assertThrows(IllegalStateException.class, () ->
                reservationService.permitReservation(findReservationId1));

        assertThrows(IllegalStateException.class, () ->
                reservationService.completeReservation(findReservationId1));

    }

    @Test
    public void 예약_조회() {
        // 예약 번호로 예약 조회 기능
        // 사용자에 대한 모든 예약 조회
        // 버스에 대한 모든 예약 조회

        // Given
        String memberId1 = createMember("정영한");
        String memberId2 = createMember("정연준");

        Long busId1 = createBus("1");
        Long busId2 = createBus("2");
        Long busId3 = createBus("3");
        Long busId4 = createBus("4");
        Long busId5 = createBus("5");

        // When
        /*reservationService.saveReservation(memberId1, busId1, "구미역", "금오공대", LocalDate.now());
        reservationService.saveReservation(memberId1, busId2, "구미역", "금오공대", LocalDate.now().plusDays(10));
        reservationService.saveReservation(memberId1, busId3, "구미역", "금오공대", LocalDate.now().plusMonths(1));
        reservationService.saveReservation(memberId1, busId4, "구미역", "금오공대", LocalDate.now().plusDays(8));
        reservationService.saveReservation(memberId1, busId5, "구미역", "금오공대", LocalDate.now().plusDays(4));

        reservationService.saveReservation(memberId2, busId1, "구미역", "금오공대", LocalDate.now());*/

        em.flush();
        em.clear();


        // Then
        Member findMember = memberRepository.findOne(memberId1);

        // 사용자에 대한 모든 예약 조회
        List<Reservation> reservationsByMember = reservationService.findReservationsByMember(memberId1);

        assertThat(reservationsByMember.get(0).getBus().getBusNumber()).isEqualTo("3");
        assertThat(reservationsByMember.get(1).getBus().getBusNumber()).isEqualTo("2");
        assertThat(reservationsByMember.get(2).getBus().getBusNumber()).isEqualTo("4");
        assertThat(reservationsByMember.get(3).getBus().getBusNumber()).isEqualTo("5");
        assertThat(reservationsByMember.get(4).getBus().getBusNumber()).isEqualTo("1");

        // 버스에 대한 모든 예약 조회
        List<Reservation> reservationsByBus = reservationService.findReservationsByBus(busId1);

        assertThat(reservationsByBus.get(0).getMember().getName()).isEqualTo("정영한");
        assertThat(reservationsByBus.get(1).getMember().getName()).isEqualTo("정연준");
    }

    @Test
    public void checkScheduleDuplication_검증() {

        // Given
        String memberId1 = createMember("정영한");

        Long busId1 = createBus("1");

        // When
//        Long reservationId = reservationService.saveReservation(memberId1, busId1, "구미역", "금오공대", LocalDate.now());

//        reservationService.cancelReservation(reservationId);

//        Long reservationId2 = reservationService.saveReservation(memberId1, busId1, "구미역", "금오공대", LocalDate.now());

        em.flush();
        em.clear();

//        Reservation reservation = reservationRepository.findOne(reservationId);

        // Then
//        List<Reservation> reservations = reservationRepository.checkScheduleDuplication(memberId1, busId1, reservation.getReservationDate());

        /*for (Reservation reservation1 : reservations) {
            System.out.println("reservation1.getReservationDate() = " + reservation1.getReservationDate());
        }*/
    }

    @Test
    public void 예약_제약_검증() {
        // Given
        String memberId1 = createMember("정영한");
        Long busId1 = createBus("1");

        String memberId2 = createMember("정연준");
        Long busId2 = createBus("2");

        // When
//        reservationService.saveReservation(memberId1, busId1, "구미역", "금오공대", LocalDate.now());
//        Long reservationId1 = reservationService.saveReservation(memberId2, busId2, "구미역", "금오공대", LocalDate.now());

        em.flush();
        em.clear();

//        reservationService.cancelReservation(reservationId1);

        em.flush();
        em.clear();

//        Long reservationId2 = reservationService.saveReservation(memberId2, busId2, "구미역", "금오공대", LocalDate.now());

        em.flush();
        em.clear();

        // Then
        /*assertThrows(IllegalStateException.class, () ->
                reservationService.saveReservation(memberId1, busId1, "형곡2동", "금오공대", LocalDate.now())
        );*/

    }

    @Test
    public void 버스기사_예약_조회() {

        // Given
        String memberId1 = createMember("정영한");
        String memberId2 = createMember("정연준");
        String memberId3 = createMember("홍길동");

        Long busId1 = createBus("1");

        // When
        /*reservationService.saveReservation(memberId1, busId1, "구미역", "금오공대", LocalDate.now());
        reservationService.saveReservation(memberId1, busId1, "구미역", "금오공대", LocalDate.now().plusDays(10));
        reservationService.saveReservation(memberId2, busId1, "구미역", "금오공대", LocalDate.now());
        reservationService.saveReservation(memberId2, busId1, "구미역", "금오공대", LocalDate.now().plusDays(10));
        reservationService.saveReservation(memberId2, busId1, "구미역", "금오공대", LocalDate.now().plusDays(11));*/

        List<Reservation> reservations = reservationService.checkScheduleByBus(busId1, LocalDate.now());

        // Then
        for (Reservation reservation : reservations) {
            System.out.println("reservation.getMember().getName() = " + reservation.getMember().getName());
        }

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

    private Long createBus(String busNum) {
        Bus bus = new Bus();
        bus.setId(bId++);
        bus.setBusNumber(busNum);
        bus.setDirection(BusState.FORWARD);
        bus.setBusAllocationSeq(seq++);
        bus.setDepartureTime(LocalTime.now().plusMinutes(10));
        em.persist(bus);

        return bus.getId();
    }

    private Station createStation(Long id, String name) {
        return new Station(id, name);
    }

    private Route createRoute(Long id, Bus bus, Station station, int stationSeq, LocalTime arrivalTime) {
        return new Route(id, bus, station, stationSeq, arrivalTime);
    }
}