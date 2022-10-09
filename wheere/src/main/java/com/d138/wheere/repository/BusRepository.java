package com.d138.wheere.repository;

import com.d138.wheere.domain.Bus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class BusRepository {

    private final EntityManager em;

    public Bus findOne(Long id) {
        return em.find(Bus.class, id);
    }
}
