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

    /**
     * 버스 번호와 버스 방향으로 해당 버스 경로 조회
     * @param busNum
     * @param direction
     * @return
     */
    public List<Route> findBusRouteByBusInfo(String busNum, BusState direction) {

        return em.createQuery("select r from Route r" +
                        " join fetch r.station join r.bus b on b.busNumber = :bNum" +
                        " and b.direction = :bDir" +
                        " and b.busAllocationSeq = 1" +
                        " order by r.stationSeq", Route.class)
                .setParameter("bNum", busNum)
                .setParameter("bDir", direction)
                .getResultList();
    }

    /**
     * 버스 ID (PK), 정류장 이름으로 Route 조회
     * 예약할 때 JSON 스펙으로 정류장 이름을 받아오면 해당 메서드 호출
     * 아니면 findRouteBySeq 호출
     *
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

    /**
     * 출발, 도착 정류장 조회
     * 버스 예약할 때 사용됨
     * @param busId
     * @param seqList
     * @return 출발, 도착 정류장
     */
    public List<Route> findRoutesByBusAndSeq(Long busId, List<Integer> seqList) {
        return em.createQuery("select r from Route r"
                        + " join r.bus b on b.id = :busId"
                        + " join fetch r.station s"
                        + " where r.stationSeq in :seqList", Route.class)
                .setParameter("busId", busId)
                .setParameter("seqList", seqList)
                .getResultList();
    }

    /**
     * 출발 정류장, 도착 정류장에 대한 정류장 순번 얻기
     * 예약 취소 (CANCEL) 시 호출됨
     * @param busId
     * @param pointList
     * @return
     */
    public List<Integer> findSeqByBusAndName(Long busId, List<String> pointList) {
        return em.createQuery("select r.stationSeq from Route r"
                        + " join r.bus b on b.id = :busId"
                        + " where r.station.name in :pointList", Integer.class)
                .setParameter("busId", busId)
                .setParameter("pointList", pointList)
                .getResultList();
    }

    // TODO (어..)
    public List<Object[]> inquiryBusSchedule(String busNum, BusState direction, List<Integer> seqList) {
        return em.createQuery("select b.id, r.arrivalTime" +
                        " from Route r" +
                        " join r.bus b on b.busNumber = :busNum and b.direction = :direction" +
                        " where r.stationSeq in :seqList", Object[].class)
                .setParameter("busNum", busNum)
                .setParameter("direction", direction)
                .setParameter("seqList", seqList)
                .getResultList();
    }
}
