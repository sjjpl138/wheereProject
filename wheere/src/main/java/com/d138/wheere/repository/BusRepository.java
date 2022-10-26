package com.d138.wheere.repository;

import com.d138.wheere.domain.Bus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    // 버스 번호, 방향, 배차 순번를 이용해 버스 조회
}
