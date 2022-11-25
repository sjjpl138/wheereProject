package com.d138.wheere.repository;

import com.d138.wheere.domain.BusState;
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

    public Seat findSeatByRouteAndDate(Long routeId, LocalDate reservationDate) {
        return em.createQuery("select s from Seat s"
                        + " join s.route r on r.id = :routeId"
                        + " where s.reservationDate = :rDate", Seat.class)
                .setParameter("routeId", routeId)
                .setParameter("rDate", reservationDate)
                .getSingleResult();
    }

    public List<Seat> findSeatsByBusAndDate(Long busId, LocalDate reservationDate, List<Integer> seqList) {
        return em.createQuery("select s from Seat s"
                        + " join s.route r on r.bus.id = :busId and r.stationSeq in :seqList"
                        + " where s.reservationDate = :date", Seat.class)
                .setParameter("busId", busId)
                .setParameter("seqList", seqList)
                .setParameter("date", reservationDate)
                .getResultList();
    }

    // TODO (어..)
    public List<Integer> inquiryMinLeftSeatNum(Long busId, LocalDate reservationDate, List<Integer> seqList) {
        return em.createQuery("select min(s.leftSeatsNum)" +
                        " from Seat s" +
                        " join s.route r on r.stationSeq in :seqList" +
                        " join r.bus b on b.id = :busId" +
                        " where s.reservationDate = :reservationDate", Integer.class)
                .setParameter("busId", busId)
                .setParameter("seqList", seqList)
                .setParameter("reservationDate", reservationDate)
                .getResultList();
    }
}
