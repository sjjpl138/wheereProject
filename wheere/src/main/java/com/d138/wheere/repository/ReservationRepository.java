package com.d138.wheere.repository;

import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

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

    public List<Reservation> findAll() {
        return em.createQuery("select r from Reservation r", Reservation.class)
                .getResultList();
    }

    // MemberId로 예약 조회하기
    public List<Reservation> findByMember(String memberId) {
        return em.createQuery("select r from Reservation r join r.member m where m.id = :memberId", Reservation.class)
                .setParameter("memberId", memberId).getResultList();
    }

    public List<Reservation> findByBus(Long busId) {
        return em.createQuery("select r from Reservation r join r.bus b where b.id = :busId", Reservation.class)
                .setParameter("busId", busId).getResultList();
    }

    // getSingleList(); 로 변경해도 되지 않을지 검사 (같은 날짜에 동일 버스를 여러 번 예약 취소했을 경우도 존재하므로 안 됨)
    public List<Reservation> checkScheduleDuplication(String memberId, Long busId, LocalDate reservationDate) {

        return em.createQuery("select r from Reservation r join r.bus b join r.member m where b.id = :busId and m.id = :memberId and r.reservationDate = :reservationDate", Reservation.class)
                .setParameter("busId", busId)
                .setParameter("memberId", memberId)
                .setParameter("reservationDate", reservationDate)
                .getResultList();
    }
}
