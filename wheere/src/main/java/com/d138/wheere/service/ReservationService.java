package com.d138.wheere.service;

import com.d138.wheere.domain.*;
import com.d138.wheere.exception.NotEnoughSeatsException;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.MemberRepository;
import com.d138.wheere.repository.ReservationRepository;
import com.d138.wheere.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final BusRepository busRepository;

    private final SeatRepository seatRepository;

    /**
     * 예약
     */
    @Transactional
    public Long saveReservation(String memberId, Long busId, String startPoint, String endPoint
            , LocalDate reservationDate) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Bus bus = busRepository.findOne(busId); // 여기 버스가 버스 테이블의 버스가 아닌 예약에 참조된 버스여야 할듯??

         /* 버스 제약사항 추가 */

        // 예약하려는 버스 출발 시간이 현재 시간 이전이라면 예약 불가
        if ((LocalTime.now().isAfter(bus.getDepartureTime())) && (LocalDate.now().isAfter(reservationDate) || LocalDate.now().isEqual(reservationDate))) {
            throw new IllegalStateException("해당 버스에 대해 예약이 불가능합니다.");
        }

        // 예약하려는 날짜에 사용자의 예약하려는 버스에 대한 예약들을 불러온다.
        List<Reservation> reservations = reservationRepository.checkScheduleDuplication(member.getId(), bus.getId(), reservationDate);

        // 동일 버스에 대한 기존 예약이 존재하고 기존 예약의 상태가 취소 상태가 아니라면 예약 불가
        if (!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                if(reservation.getReservationState() != ReservationState.CANCEL)
                    throw new IllegalStateException("이미 해당 버스에 대한 예약이 존재합니다.");
            }
        }

        // 예약 생성
        Reservation reservation = Reservation.createReservation(member, bus, startPoint, endPoint, reservationDate);
        // 해당 버스 좌석 감소
        // 예약하려는 날짜, 버스에 대한 Seat이 만들어져 있지 않으면 생성??
        // 날짜와 버스Id로 Seat 조회하는 메서드 작성해야 할 듯
        List<Seat> findSeat = seatRepository.findByBusAndDate(busId, reservationDate);
        if (findSeat.isEmpty()) {
            Seat seat = Seat.createSeat(bus, reservationDate, bus.getTotalWheelChairSeats());
            seat.subSeats();
            seatRepository.save(seat);
        } else {
            findSeat.get(0).subSeats();
        }

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

    // 예약 거절
    @Transactional
    public void rejectReservation(Long reservationId) {
        Reservation findReservation = reservationRepository.findOne(reservationId);
    }

    public Reservation findReservation(Long reservationId) {
        return reservationRepository.findOne(reservationId);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    // 특정 사용자에 대한 모든 예약 정보 조회
    public List<Reservation> findReservationsByMember(String memberId) {
        return reservationRepository.findByMember(memberId);
    }

    // 특정 버스에 대한 모든 예약 정보 조회
    public List<Reservation> findReservationsByBus(Long busId) {
        return reservationRepository.findByBus(busId);
    }

    // 특정 버스, 특정 날짜에 대한 예약 검색
    public List<Reservation> checkScheduleByBus(Long busId, LocalDate date) {
        return reservationRepository.findByBusAndDate(busId, date);
    }
}
