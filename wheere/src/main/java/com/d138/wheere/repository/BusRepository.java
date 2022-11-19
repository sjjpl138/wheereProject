package com.d138.wheere.repository;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BusRepository {

    private final EntityManager em;

    /**
     * 버스 ID (PK)로 버스 조회
     * @param busId
     * @return
     */
    public Bus findOne(Long busId) {

        return em.find(Bus.class, busId);
    }

    /**
     * 버스 번호, 방향, 출발 시간으로 버스 조회
     * 버스 예약, 버스 배차에 사용됨
     * @param busNum 버스 번호
     * @param busState 버스 방향 (정방향, 역방향)
     * @param departureTime (출발 시간)
     * @return 조회하려는 버스
     */
    public Bus findBus(String busNum, BusState busState, LocalTime departureTime) {
        return em.createQuery("select b from Bus b where b.busNumber = :busNum and b.direction = :busState and b.departureTime = :departureTime", Bus.class)
                .setParameter("busNum", busNum)
                .setParameter("busState", busState)
                .setParameter("departureTime", departureTime)
                .getSingleResult();
    }

    // 버스 출발 시간 조회 메서드
    public List<LocalTime> findDepartureTime(String busNum, BusState direction) {
        return em.createQuery("select b.departureTime from Bus b where b.busNumber = :busNum and b.direction = :direction", LocalTime.class)
                .setParameter("busNum", busNum)
                .setParameter("direction", direction)
                .getResultList();
    }
}
