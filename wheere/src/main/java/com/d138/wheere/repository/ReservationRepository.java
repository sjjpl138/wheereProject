package com.d138.wheere.repository;

import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.domain.ReservationState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {

    private final EntityManager em;

    public Long save(Reservation reservation) {
        em.persist(reservation);
        return reservation.getId();
    }

    public Reservation findOne(Long id) {
        return em.find(Reservation.class, id);
    }

    // MemberId로 예약 조회하기
    public List<Reservation> findByMember(String memberId) {

        List<ReservationState> reservationStates = Arrays.asList(ReservationState.RESERVED, ReservationState.WAITING);

        return em.createQuery("select r from Reservation r" +
                        " join r.member m on m.id = :memberId" +
                        " join fetch r.bus" +
                        " where r.reservationState in :reservationStates" +
                        " ORDER BY r.reservationDate DESC", Reservation.class)
                .setParameter("memberId", memberId)
                .setParameter("reservationStates", reservationStates)
                .getResultList();
    }

    // 특정 버스, 특정 날짜에 대한 예약 검색
    public List<Reservation> findByBusAndDate(Long busId, LocalDate reservationDate) {

        List<ReservationState> reservationStates = Arrays.asList(ReservationState.RESERVED, ReservationState.WAITING, ReservationState.COMP);

        return em.createQuery("select r from Reservation r"
                                + " join r.bus b on b.id = :busId"
                                + " where r.reservationDate = :date"
                                + " and r.reservationState in :reservationStates"
                        , Reservation.class)
                .setParameter("busId", busId)
                .setParameter("date", reservationDate)
                .setParameter("reservationStates", reservationStates)
                .getResultList();
    }

    /**
     * 사용자가 같은 버스를 동일한 날짜에 예약한적이 있는지 검증
     *
     * @param memberId
     * @param busId
     * @param reservationDate
     * @return 특정 날짜 특정 버스에 대한 사용자의 예약 기록
     */
    public List<Reservation> checkScheduleDuplication(String memberId, Long busId, LocalDate reservationDate) {
        return em.createQuery("select r from Reservation r" +
                        " join r.bus b on b.id = :busId" +
                        " join r.member m on m.id = :memberId" +
                        " where r.reservationDate = :reservationDate", Reservation.class)
                .setParameter("busId", busId)
                .setParameter("memberId", memberId)
                .setParameter("reservationDate", reservationDate)
                .getResultList();
    }
}
