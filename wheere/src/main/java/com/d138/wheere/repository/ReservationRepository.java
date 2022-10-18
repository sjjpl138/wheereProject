package com.d138.wheere.repository;

import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

    public List<Reservation> checkScheduleDuplication(String memberId, Long busId) {

        return em.createQuery("select r from Reservation r join r.bus b join r.member m where b.id = :busId and m.id = :memberId", Reservation.class)
                .setParameter("busId", busId)
                .setParameter("memberId", memberId)
                .getResultList();
    }
}
