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

    public Long save(Bus bus) {
        em.persist(bus);

        return bus.getId();
    }

    public Bus findOne(Long busId) {

        return em.find(Bus.class, busId);
    }

    // 사용자 또는 버스 기사가 버스 번호, 방향, 출발 시간으로 버스 선택
    // 버스 예약, 버스 배차에 사용됨
    public Bus findBus(String busNum, BusState busState, LocalTime departureTime) {
        return em.createQuery("select b from Bus b where b.busNumber = :busNum and b.direction = :busState and b.departureTime = :departureTime", Bus.class)
                .setParameter("busNum", busNum)
                .setParameter("busState", busState)
                .setParameter("departureTime", departureTime)
                .getSingleResult();
    }

    // 버스 번호, 방향만 조회하는 메서드
    public List<BusNumDirDTO> findBusNumDir() {
        return em.createQuery("select new com.d138.wheere.repository.BusNumDirDTO(b.busNumber, b.direction) from Bus b where b.busAllocationSeq = 1 order by (b.busNumber, b.direction)", BusNumDirDTO.class)
                .getResultList();
    }

    // 버스 출발 시간 조회 메서드
    public List<LocalTime> findDepartureTime(String busNum, BusState direction) {
        return em.createQuery("select b.departureTime from Bus b where b.busNumber = :busNum and b.direction = :direction", LocalTime.class)
                .setParameter("busNum", busNum)
                .setParameter("direction", direction)
                .getResultList();
    }
}
