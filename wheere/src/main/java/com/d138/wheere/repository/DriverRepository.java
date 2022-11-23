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

    public Driver findOne(String driverId) {
        return em.find(Driver.class, driverId);
    }
}
