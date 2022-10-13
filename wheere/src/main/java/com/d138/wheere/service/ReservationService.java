package com.d138.wheere.service;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.Member;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.domain.ReservationState;
import com.d138.wheere.exception.NotEnoughSeatsException;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.MemberRepository;
import com.d138.wheere.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final BusRepository busRepository;

    /**
     * 예약
     */
    @Transactional
    public Long saveReservation(Long memberId, Long busId, String startPoint, String endPoint
            , LocalDateTime reservationDate) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Bus bus = busRepository.findOne(busId);

        // 버스 제약사항 추가
        // 만약 동일한 배차순번, 버스Id를 가지면 예약 불가

        // 예약 생성
        Reservation reservation = Reservation.createReservation(member, bus, startPoint, endPoint, reservationDate);

        // 해당 버스 좌석 감소
        /*try {
            bus.subSeats();
            System.out.println("bus.getLeftWheelChairSeats() = " + bus.getLeftWheelChairSeats());
        } catch (NotEnoughSeatsException e) {
            e.printStackTrace();
            System.out.println("예약이 불가합니다.");
        }*/

        bus.subSeats();

        reservationRepository.save(reservation);

        return reservation.getId();
    }

    /**
     * 예약 취소
     */
    @Transactional
    public void cancelReservation(Long reservationId) {
        // 예약 엔티티 조회
        Reservation findReservation = reservationRepository.findOne(reservationId);

        findReservation.cancel();
    }

    public Reservation findReservation(Long reservationId) {
        return reservationRepository.findOne(reservationId);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    // 특정 사용자에 대한 모든 예약 정보 조회
    public List<Reservation> findReservationsByMember(Long memberId) {
        return reservationRepository.findByMember(memberId);
    }

    // 특정 버스에 대한 모든 예약 정보 조회
    public List<Reservation> findReservationsByBus(Long busId) {
        return reservationRepository.findByBus(busId);
    }
}
