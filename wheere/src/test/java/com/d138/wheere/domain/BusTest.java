package com.d138.wheere.domain;

import com.d138.wheere.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public class BusTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    BusService busService;

    @Autowired
    BusRepository busRepository;
}
