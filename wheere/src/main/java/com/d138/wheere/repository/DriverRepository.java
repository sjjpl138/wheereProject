package com.d138.wheere.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class DriverRepository {

    //testTest
    @PersistenceContext
    private EntityManager em;
}
