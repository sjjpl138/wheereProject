package com.d138.wheere.service;

import com.d138.wheere.domain.*;
import com.d138.wheere.exception.NotEnoughSeatsException;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.MemberRepository;
import com.d138.wheere.repository.ReservationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
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

    @Test
    public void 예약하기() {

        // Given
        Long memberId = createMember("정영한");

        Long busId = createBus("191");

        // When
        Long reservationId = reservationService.saveReservation(memberId, busId, "구미역", "금오공대",
                LocalDateTime.now());

        // Then
        Reservation findReservation = reservationRepository.findOne(reservationId);

        // 예약시 상태는 대기 (WAITING) 상태여야 한다.
        assertThat(findReservation.getReservationState()).isEqualTo(ReservationState.WAITING);

        // 예약시 버스 좌석은 하나 줄어들어야 한다.
        Bus findBus = busRepository.findOne(busId);
        assertThat(findBus.getLeftWheelChairSeats()).isEqualTo(1);

    }

    @Test
    public void 예약_예외_발생() {
        // 남은 버스 좌석이 0인 경우 예약이 불가해야 한다.

        // Given
        Long memberId1 = createMember("정영한");
        Long memberId2 = createMember("정연준");
        Long memberId3 = createMember("손지민");

        Long busId = createBus("191");
        Bus findBus = busRepository.findOne(busId);

        // When
        Long reservationId1 = reservationService.saveReservation(memberId1, busId, "구미역", "금오공대",
                LocalDateTime.now());
        System.out.println("findBus.getLeftWheelChairSeats() = " + findBus.getLeftWheelChairSeats());
        Long reservationId2 = reservationService.saveReservation(memberId2, busId, "구미역", "금오공대",
                LocalDateTime.now());
        System.out.println("findBus.getLeftWheelChairSeats() = " + findBus.getLeftWheelChairSeats());

        // Then
        assertThrows(NotEnoughSeatsException.class, () ->
                reservationService.saveReservation(memberId3, busId, "구미역", "금오공대",
                        LocalDateTime.now())
        );
    }

    @Test
    public void 예약_취소() {
        // Given
        Long memberId = createMember("정영한");

        Long busId = createBus("191");

        Bus findBus = busRepository.findOne(busId);

        // When
        Long reservationId = reservationService.saveReservation(memberId, busId, "구미역", "금오공대",
                LocalDateTime.now());

        // 예약 취소
        reservationService.cancelReservation(reservationId);

        // Then
        // 예약 상태 변경 (xxx -> CANCEL) 확인
        Reservation findReservation = reservationRepository.findOne(reservationId);
        assertThat(findReservation.getReservationState()).isEqualTo(ReservationState.CANCEL);

        // 버스 좌석 수 증가
        assertThat(findBus.getLeftWheelChairSeats()).isEqualTo(2);

        findReservation.canCancel();

        // 남은 좌석 수가 이미 total 일 경우 예약을 취소해도 남은 좌석 수가 증가하지 않는다.
        /*reservationService.cancelReservation(reservationId);
        assertThat(findBus.getLeftWheelChairSeats()).isEqualTo(2);*/
    }

    @Test
    public void 예약_취소_예외() {
        // Given
        Long memberId = createMember("정영한");

        Long busId = createBus("191");

        Bus findBus = busRepository.findOne(busId);

        // When
        Long reservationId = reservationService.saveReservation(memberId, busId, "구미역", "금오공대",
                LocalDateTime.now());

        Reservation findReservation = reservationRepository.findOne(reservationId);

        reservationService.cancelReservation(reservationId);

        System.out.println("findReservation.getReservationState() = " + findReservation.getReservationState());

        // Then
        // 이미 예약이 취소가 된 상태라면 예약 취소가 불가능해야 한다.
        assertThrows(IllegalStateException.class, () ->
                reservationService.cancelReservation(reservationId)
        );
    }

    @Test
    public void 예약_조회() {
        // 예약 번호로 예약 조회 기능
        // 사용자에 대한 모든 예약 조회
        // 버스에 대한 모든 예약 조회

        // Given
        Long memberId1 = createMember("정영한");
        Long memberId2 = createMember("정연준");

        Long busId1 = createBus("1");
        Long busId2 = createBus("2");
        Long busId3 = createBus("3");
        Long busId4 = createBus("4");
        Long busId5 = createBus("5");

        // When
        reservationService.saveReservation(memberId1, busId1, "구미역", "금오공대", LocalDateTime.now());
        reservationService.saveReservation(memberId1, busId2, "구미역", "금오공대", LocalDateTime.now());
        reservationService.saveReservation(memberId1, busId3, "구미역", "금오공대", LocalDateTime.now());
        reservationService.saveReservation(memberId1, busId4, "구미역", "금오공대", LocalDateTime.now());
        reservationService.saveReservation(memberId1, busId5, "구미역", "금오공대", LocalDateTime.now());

        reservationService.saveReservation(memberId2, busId1, "구미역", "금오공대", LocalDateTime.now());


        // Then
        Member findMember = memberRepository.findOne(memberId1);

        // 사용자에 대한 모든 예약 조회
        List<Reservation> reservationsByMember = reservationService.findReservationsByMember(memberId1);

        assertThat(reservationsByMember.get(0).getBus().getBusNumber()).isEqualTo("1");
        assertThat(reservationsByMember.get(1).getBus().getBusNumber()).isEqualTo("2");
        assertThat(reservationsByMember.get(2).getBus().getBusNumber()).isEqualTo("3");
        assertThat(reservationsByMember.get(3).getBus().getBusNumber()).isEqualTo("4");
        assertThat(reservationsByMember.get(4).getBus().getBusNumber()).isEqualTo("5");

        // 버스에 대한 모든 예약 조회
        List<Reservation> reservationsByBus = reservationService.findReservationsByBus(busId1);

        assertThat(reservationsByBus.get(0).getMember().getName()).isEqualTo("정영한");
        assertThat(reservationsByBus.get(1).getMember().getName()).isEqualTo("정연준");

        // 모든 버스 예약 조회
        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations.get(0).getBus().getBusNumber()).isEqualTo("1");
        assertThat(reservations.get(1).getBus().getBusNumber()).isEqualTo("2");
        assertThat(reservations.get(2).getBus().getBusNumber()).isEqualTo("3");
        assertThat(reservations.get(3).getBus().getBusNumber()).isEqualTo("4");
        assertThat(reservations.get(4).getBus().getBusNumber()).isEqualTo("5");
        assertThat(reservations.get(5).getBus().getBusNumber()).isEqualTo("1");
    }

    private Long createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAge(22);
        member.setPhoneNumber("010-1111-1111");

        em.persist(member);
        return member.getId();
    }

    private Long createBus(String busNum) {
        Driver driver = new Driver();
        driver.setName("정영한");
        driver.setRatingCnt(5);
        driver.setRatingScore(4.0);
        em.persist(driver);

        Bus bus = new Bus();
        bus.setBusNumber(busNum);
        bus.setDriver(driver);
        bus.setTotalWheelChairSeats(2);
        bus.setLeftWheelChairSeats(2);

        em.persist(bus);
        return bus.getId();
    }
}