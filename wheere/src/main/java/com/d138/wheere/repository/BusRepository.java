package com.d138.wheere.repository;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalTime;

@Repository
@RequiredArgsConstructor
public class BusRepository {

    private final EntityManager em;

    public Long save(Bus bus) {
        em.persist(bus);

        return bus.getId();
    }

    public Bus findOne(Long busId) {

        return em.find(Bus.class, busId);
    }

    // 버스 번호, 방향, 출발 시간을 이용해 버스 조회 (버스 기사 버스 배차용)
    public Bus findBus(String busNum, BusState busState, LocalTime departureTime) {
        return em.createQuery("select b from Bus b where b.busNumber = :busNum and b.direction = :busState and b.departureTime = :departureTime", Bus.class)
                .setParameter("busNum", busNum)
                .setParameter("busState", busState)
                .setParameter("departureTime", departureTime)
                .getSingleResult();
    }
}
