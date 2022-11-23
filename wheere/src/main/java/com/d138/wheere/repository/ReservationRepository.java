package com.d138.wheere.repository;

import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
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
        return em.createQuery("select r from Reservation r" +
                        " join r.member m on m.id = :memberId" +
                        " join fetch r.bus" +
                        " ORDER BY r.reservationDate DESC", Reservation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    // 특정 버스, 특정 날짜에 대한 예약 검색
    // TODO (정류소 순번별로 정렬해야 함)
    public List<Reservation> findByBusAndDate(Long busId, LocalDate reservationDate) {
        return em.createQuery("select r from Reservation r"
                                + " join r.bus b on b.id = :busId"
                                + " where r.reservationDate = :date"
                        , Reservation.class)
                .setParameter("busId", busId)
                .setParameter("date", reservationDate)
                .getResultList();
    }

    /**
     * 사용자가 같은 버스를 동일한 날짜에 예약한적이 있는지 검증
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
