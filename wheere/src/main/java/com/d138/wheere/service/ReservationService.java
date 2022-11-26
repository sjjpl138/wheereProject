package com.d138.wheere.service;

import com.d138.wheere.domain.*;
import com.d138.wheere.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final SeatRepository seatRepository;

    /**
     * 예약 생성
     * 사용자가 예약 UI 화면에서 정보 입력 후 예약하기 클릭 시 호출됨
     *
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

        // 출발 정류장, 도착 정류장 조회
        List<Route> findRoutes = getStartEndRoutes(busId, startSeq, endSeq);

        // 출발 정류장
        Route startRoute = findRoutes.get(0);
        // 도착 정류장
        Route endRoute = findRoutes.get(1);

        /* 버스 제약사항 추가 */
        // 예약하려는 버스 출발 시간이 현재 시간 이전이라면 예약 불가
        compareBusDepartureTime(startRoute, reservationDate);

        // 동일 버스에 대한 기존 예약이 존재하고 기존 예약의 상태가 취소 상태가 아니라면 예약 불가
        validateDuplicateReservation(reservationDate, member, bus);

        // 예약 생성
        String startPoint = startRoute.getStation().getName();
        String endPoint = endRoute.getStation().getName();

        Reservation reservation = Reservation.createReservation(member, bus, startPoint, endPoint, reservationDate);

        // 해당 버스 정류장별로 좌석 감소 (마지막 정류장 제외)
        subSeatsPerRoute(startSeq, endSeq, reservationDate, bus);

        reservationRepository.save(reservation);
        return reservation.getId();
    }

    private void subSeatsPerRoute(int startSeq, int endSeq, LocalDate reservationDate, Bus bus) {
        List<Integer> seqList = new ArrayList<>();
        for (int i = startSeq; i <= endSeq; i++)
            seqList.add(i);

        List<Seat> findSeats = seatRepository.findSeatsByBusAndDate(bus.getId(), reservationDate, seqList);

        // 예약하려는 버스에 대한 Seat이 생성되어 있지 않다면
        if (findSeats.isEmpty()) {
            createSeatPerRoute(reservationDate, bus);
            List<Seat> seats = seatRepository.findSeatsByBusAndDate(bus.getId(), reservationDate, seqList);
            calSubSeats(seats);
        } else {
            calSubSeats(findSeats);
        }
    }

    private void createSeatPerRoute(LocalDate reservationDate, Bus bus) {
        List<Route> routes = bus.getRoutes();
        int totalSeatsNum = 2;
        for (Route route : routes) {
            Seat seat = Seat.createSeat(route, totalSeatsNum, reservationDate);
            seatRepository.save(seat);
        }
    }

    private void calSubSeats(List<Seat> seats) {
        for (int i = 0; i < seats.size() - 1; i++) {
            seats.get(i).subSeats();
        }
    }

    private List<Route> getStartEndRoutes(Long busId, int startSeq, int endSeq) {
        List<Integer> seqList = List.of(startSeq, endSeq);

        List<Route> findRoutes = routeRepository.findRoutesByBusAndSeq(busId, seqList);
        return findRoutes;
    }

    private void validateDuplicateReservation(LocalDate reservationDate, Member member, Bus bus) {
        // 예약하려는 날짜에 사용자의 예약하려는 버스에 대한 예약들을 불러온다.
        System.out.println("test");
        List<Reservation> reservations = reservationRepository.checkScheduleDuplication(member.getId(), bus.getId(), reservationDate);
        for (Reservation reservation : reservations) {
            System.out.println("reservation = " + reservation);
        }
        if (!reservations.isEmpty()) {
            for (Reservation reservation : reservations) {
                if (reservation.getReservationState() != ReservationState.CANCEL)
                    throw new IllegalStateException("이미 해당 버스에 대한 예약이 존재합니다.");
            }
        }
    }

    private void compareBusDepartureTime(Route route, LocalDate reservationDate) {
        if ((LocalTime.now().isAfter(route.getArrivalTime())&& (!LocalDate.now().isBefore(reservationDate))) || (LocalDate.now().isAfter(reservationDate)))
            throw new IllegalStateException("해당 버스에 대해 예약이 불가능합니다.");
    }

    /**
     * 예약 취소
     *
     * @param reservationId
     */
    @Transactional
    public void cancelReservation(Long reservationId) {
        // 예약 엔티티 조회
        Reservation findReservation = reservationRepository.findOne(reservationId);

        Long busId = findReservation.getBus().getId();
        LocalDate reservationDate = findReservation.getReservationDate();

        findReservation.cancel();

        // 버스 좌석 증가
        addSeatsPerRoute(findReservation, busId, reservationDate);
    }

    /**
     * 예약 거절
     *
     * @param reservationId
     */
    @Transactional
    public void rejectReservation(Long reservationId) {
        // 예약 엔티티 조회
        Reservation findReservation = reservationRepository.findOne(reservationId);

        Long busId = findReservation.getBus().getId();
        LocalDate reservationDate = findReservation.getReservationDate();

        findReservation.reject();

        // 버스 좌석 증가
        addSeatsPerRoute(findReservation, busId, reservationDate);
    }

    private void addSeatsPerRoute(Reservation findReservation, Long busId, LocalDate reservationDate) {
        String startPoint = findReservation.getStartPoint();
        String endPoint = findReservation.getEndPoint();

        List<String> pointList = List.of(startPoint, endPoint);
        List<Integer> stationSeqs = routeRepository.findSeqByBusAndName(busId, pointList);

        List<Integer> seqList = new ArrayList<>();
        for (int i = stationSeqs.get(0); i <= stationSeqs.get(1); i++)
            seqList.add(i);


        List<Seat> findSeats = seatRepository.findSeatsByBusAndDate(busId, reservationDate, seqList);
        for (int i = 0; i < findSeats.size() - 1; i++) {
            findSeats.get(i).addSeats();
        }
    }

    /**
     * 예약 승인
     * @param reservationId
     */
    @Transactional
    public void permitReservation(Long reservationId) {
        // 예약 엔티티 조회
        Reservation findReservation = reservationRepository.findOne(reservationId);

        findReservation.permit();
    }

    /**
     * 운행 완료
     * @param reservationId
     */
    @Transactional
    public void completeReservation(Long reservationId) {
        // 예약 엔티티 조회
        Reservation findReservation = reservationRepository.findOne(reservationId);

        findReservation.complete();
    }

    /**
     * 예약 ID (PK)로 예약 엔티티 조회
     * @param reservationId 예약 ID (PK)
     * @return 예약 엔티티
     */
    public Reservation findReservation(Long reservationId) {
        return reservationRepository.findOne(reservationId);
    }

    /**
     * 특정 사용자에 대한 모든 예약 정보 조회
     * @param memberId
     * @return
     */
    public List<Reservation> findReservationsByMember(String memberId) {
        return reservationRepository.findByMember(memberId);
    }

    /**
     * 버스 기사가 자신이 운행하는 버스에 대한 예약 조회
     * 특정 버스, 특정 날짜에 대한 예약 검색
     * @param busId
     * @param reservationDate
     * @return
     */
    public List<Reservation> checkScheduleByBus(Long busId, LocalDate reservationDate) {
        return reservationRepository.findByBusAndDate(busId, reservationDate);
    }
}
