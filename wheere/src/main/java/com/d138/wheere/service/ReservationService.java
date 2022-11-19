package com.d138.wheere.service;

import com.d138.wheere.domain.*;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.MemberRepository;
import com.d138.wheere.repository.ReservationRepository;
import com.d138.wheere.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final BusRepository busRepository;

    private final RouteRepository routeRepository;

    /**
     * 예약 생성
     * 사용자가 예약 UI 화면에서 정보 입력 후 예약하기 클릭 시 호출됨
     * @param memberId
     * @param busId
     * @param startSeq
     * @param endSeq
     * @param reservationDate
     * @return
     */
    @Transactional
    public Long saveReservation(String memberId, Long busId, int startSeq, int endSeq, LocalDate reservationDate) {

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Bus bus = busRepository.findOne(busId);

        // 출발 정류장, 도착 정류장 순번
        List<Route> findRoutes = routeRepository.findRoutesBySeq(busId, startSeq, endSeq);

        Route startRoute = findRoutes.get(0);
        Route endRoute = findRoutes.get(findRoutes.size() - 1);

        /* 버스 제약사항 추가 */

        // 예약하려는 버스 출발 시간이 현재 시간 이전이라면 예약 불가
        // TODO (버스 출발 시간이 아닌 예상 도착 시간으로 비교하기)
        compareBusDepartureTime(startRoute, reservationDate);

        // 동일 버스에 대한 기존 예약이 존재하고 기존 예약의 상태가 취소 상태가 아니라면 예약 불가
        validateDuplicateReservation(reservationDate, member, bus);

        // 예약 생성
        String startPoint = startRoute.getStation().getName();
        String endPoint = endRoute.getStation().getName();
        Reservation reservation = Reservation.createReservation(member, bus, startPoint, endPoint, reservationDate);

        // 해당 버스 정류장별로 좌석 감소 (마지막 정류장 제외)
        subRouteSeats(findRoutes);

        reservationRepository.save(reservation);
        return reservation.getId();
    }

    private void subRouteSeats(List<Route> findRoutes) {
        for (int i = 0; i < findRoutes.size() - 1; i++) {
            findRoutes.get(i).subSeats();
        }
    }

    private void validateDuplicateReservation(LocalDate reservationDate, Member member, Bus bus) {
        // 예약하려는 날짜에 사용자의 예약하려는 버스에 대한 예약들을 불러온다.
        List<Reservation> reservations = reservationRepository.checkScheduleDuplication(member.getId(), bus.getId(), reservationDate);
        if (!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                if(reservation.getReservationState() != ReservationState.CANCEL)
                    throw new IllegalStateException("이미 해당 버스에 대한 예약이 존재합니다.");
            }
        }
    }

    private void compareBusDepartureTime(Route route, LocalDate reservationDate) {
        if ((LocalTime.now().isAfter(route.getArrivalTime())&& (!LocalDate.now().isAfter(reservationDate))) || (LocalDate.now().isAfter(reservationDate)))
            throw new IllegalStateException("해당 버스에 대해 예약이 불가능합니다.");
    }

    /**
     * 예약 취소
     */
    // TODO [예약 상태 변경 (취소, CANCEL) 구현해야 함]
    @Transactional
    public void cancelReservation(Long reservationId) {
        // 예약 엔티티 조회
        Reservation findReservation = reservationRepository.findOne(reservationId);

        findReservation.cancel();

        // 파라미터로 예약 날짜도 받아와 Seat 객체 찾고 버스 좌석 증가시켜야 함
        
    }

    // 예약 거절
    // TODO [예약 상태 변경 (거절, REFUSED) 구현해야 함]
    @Transactional
    public void rejectReservation(Long reservationId) {
        Reservation findReservation = reservationRepository.findOne(reservationId);
    }

    // TODO [예약 상태 변경 (예약됨, RESERVED) 구현해야 함]

    // TODO [예약 상태 변경 (운행 완료, COMP) 구현해야 함]

    public Reservation findReservation(Long reservationId) {
        return reservationRepository.findOne(reservationId);
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

    /**
     * 버스 기사가 자신이 운행하는 버스에 대한 예약 조회
     * @param busId
     * @param reservationDate
     * @return
     */
    public List<Reservation> checkScheduleByBus(Long busId, LocalDate reservationDate) {
        return reservationRepository.findByBusAndDate(busId, reservationDate);
    }
}
