package com.d138.wheere.repository;

import com.d138.wheere.domain.Driver;
import com.d138.wheere.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor                 
public class DriverRepository {

    private final EntityManager em;

    public Long save(Driver driver) {
        em.persist(driver);
        return driver.getId();
    }

    public Driver findOne(Long id) {
        return em.find(Driver.class, id);
    }

    public List<Driver> findByName(String name) {
        return em.createQuery("select d from Driver d where d.name = :name", Driver.class)
                .setParameter("name", name).getResultList();
    }

    public List<Driver> findAll() {
        return em.createQuery("select d from Driver d", Driver.class).getResultList();
    }
}
