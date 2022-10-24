package com.d138.wheere.repository;

import com.d138.wheere.domain.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatRepository {

    private final EntityManager em;

    public Long save(Seat seat) {
        em.persist(seat);
        return seat.getId();
    }

    public Seat findOne(Long seatId) {
        return em.find(Seat.class, seatId);
    }

    public List<Seat> findByBusAndDate(Long busId, LocalDate date) {
        return em.createQuery("select s from Seat s join s.bus b on b.id = :busId where s.operationDate = :date", Seat.class)
                .setParameter("busId", busId)
                .setParameter("date", date)
                .getResultList();
    }
}
