package com.d138.wheere.repository;

import com.d138.wheere.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public List<Reservation> findAllByMember(Long memberId) {
        return em.createQuery("select r from Reservation r where r.memberId = :mId", Reservation.class)
                .setParameter("mId", memberId).getResultList();
    }

    public List<Reservation> findAllByBus(Long busId) {
        return em.createQuery("select r from Reservation r where r.busId = :bId", Reservation.class)
                .setParameter("bId", busId).getResultList();
    }
}
