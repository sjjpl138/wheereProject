package com.d138.wheere.repository.bus.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BusQueryRepository {

    private final EntityManager em;

    // 버스 번호, 방향만 조회하는 메서드
    public List<BusNumDirDTO> findBusNumDir() {
        return em.createQuery("select new com.d138.wheere.repository.bus.query.BusNumDirDTO(b.busNumber, b.direction)"
                + " from Bus b"
                + " where b.busAllocationSeq = 1"
                + " order by b.busNumber DESC", BusNumDirDTO.class)
                .getResultList();
    }
}
