package com.d138.wheere.repository;

import com.d138.wheere.domain.Driver;
import com.d138.wheere.domain.Member;
import com.d138.wheere.domain.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor                 
public class DriverRepository {

    private final EntityManager em;

    public String save(Driver driver) {
        em.persist(driver);
        return driver.getId();
    }

    public Driver findOne(String driverId) {
        return em.find(Driver.class, driverId);
    }

    // 버스 번호, 배차순번, 방향으로 구분
    /*public List<Driver> findByBusId(Long busId) {
        return em.createQuery("select d from Driver d join d.bus  b where b.id = :busId", Driver.class)
                .setParameter("busId", busId).getResultList();
    }*/

    public List<Driver> findByName(String name) {
        return em.createQuery("select d from Driver d where d.name = :name", Driver.class)
                .setParameter("name", name).getResultList();
    }

    public List<Driver> findAll() {
        return em.createQuery("select d from Driver d", Driver.class).getResultList();
    }
}
