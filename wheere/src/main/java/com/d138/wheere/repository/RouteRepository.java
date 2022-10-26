package com.d138.wheere.repository;

import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RouteRepository {

    private final EntityManager em;

    public List<Route> findBusRoute(Long busId) {

        return em.createQuery("select r from Route r join r.bus b on b.id = :bId order by r.stationSeq", Route.class)
                .setParameter("bId", busId)
                .getResultList();
    }

    public List<Route> findBusRouteByBusInfo(String busNum, BusState busState) {

        return em.createQuery("select r from Route r join fetch r.station join r.bus b on b.busNumber = :bNum and b.direction = :bDir and b.busAllocationSeq = 1 order by r.stationSeq", Route.class)
                .setParameter("bNum", busNum)
                .setParameter("bDir", busState)
                .getResultList();
    }
}
