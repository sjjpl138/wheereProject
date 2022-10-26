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

    // N + 1 문제 발생
    public List<Route> findBusRoute(Long busId) {

        return em.createQuery("select r from Route r join fetch r.station join r.bus b on b.id = :bId order by r.stationSeq", Route.class)
                .setParameter("bId", busId)
                .getResultList();
    }

    // 버스 번호와 버스 방향으로 해당 버스 경로 조회
    public List<Route> findBusRouteByBusInfo(String busNum, BusState direction) {

        return em.createQuery("select r from Route r join fetch r.station join r.bus b on b.busNumber = :bNum and b.direction = :bDir and b.busAllocationSeq = 1 order by r.stationSeq", Route.class)
                .setParameter("bNum", busNum)
                .setParameter("bDir", direction)
                .getResultList();
    }
}
