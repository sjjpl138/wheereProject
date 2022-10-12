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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

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
    ReservationRepository reservationRepository;

    @Autowired
    BusRepository busRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 예약하기() {

        // Given
        Long memberId = createMember();

        Long busId = createBus();

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
        Long memberId1 = createMember();
        Long memberId2 = createMember();
        Long memberId3 = createMember();

        Long busId = createBus();
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
        Long memberId = createMember();

        Long busId = createBus();

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
        reservationService.cancelReservation(reservationId);
        assertThat(findBus.getLeftWheelChairSeats()).isEqualTo(2);
    }

    @Test
    public void 예약_취소_예외() {
        // Given
        Long memberId = createMember();

        Long busId = createBus();

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
        // 사용자에 대한 모든 예약 조회
        // 버스에 대한 모든 예약 조회

        // Given


        // When


        // Then

    }

    private Long createMember() {
        Member member = new Member();
        member.setName("홍길동");
        member.setAge(22);
        member.setPhoneNumber("010-1111-1111");

        em.persist(member);
        return member.getId();
    }

    private Long createBus() {
        Bus bus = new Bus();
        bus.setBusNumber("191");
        bus.setDriver(new Driver());
        bus.setTotalWheelChairSeats(2);
        bus.setLeftWheelChairSeats(2);

        em.persist(bus);
        return bus.getId();
    }
}