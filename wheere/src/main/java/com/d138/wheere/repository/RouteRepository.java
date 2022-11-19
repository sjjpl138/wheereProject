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

    // 버스 번호와 버스 방향으로 해당 버스 경로 조회
    public List<Route> findBusRouteByBusInfo(String busNum, BusState direction) {

        return em.createQuery("select r from Route r join fetch r.station join r.bus b on b.busNumber = :bNum and b.direction = :bDir and b.busAllocationSeq = 1 order by r.stationSeq", Route.class)
                .setParameter("bNum", busNum)
                .setParameter("bDir", direction)
                .getResultList();
    }

    /**
     * 버스 ID (PK), 출발 정류장 순번, 도착 정류장 순번으로 Route 조회
     * 예약할 때 JSON 스펙으로 정류장 순번 받아올 때 호출됨
     * @param busId
     * @param startSeq
     * @param endSeq
     * @return
     */
    public List<Route> findRoutesBySeq(Long busId, int startSeq, int endSeq) {
        return em.createQuery("select r from Route r"
                        + " join r.bus b on b.id = :busId"
                        + " join fetch r.station", Route.class)
                .setParameter("busId", busId)
                .setFirstResult(startSeq-1)
                .setMaxResults(endSeq - startSeq + 1)
                .getResultList();
    }

    /**
     * 버스 ID (PK), 정류장 이름으로 Route 조회
     * 예약할 때 JSON 스펙으로 정류장 이름을 받아오면 해당 메서드 호출
     * 아니면 findRouteBySeq 호출
     * @param busId
     * @param stationName
     * @return
     */
    public Route findOneByBusAndStationName(Long busId, String stationName) {
        return em.createQuery("select r from Route r"
                        + " join r.bus b on b.id = :busId"
                        + " join r.station s on s.name = :stationName", Route.class)
                .setParameter("busId", busId)
                .setParameter("stationName", stationName)
                .getSingleResult();
    }
}
