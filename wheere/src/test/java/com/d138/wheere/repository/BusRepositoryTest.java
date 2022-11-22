package com.d138.wheere.repository;

import com.d138.wheere.domain.*;
import com.d138.wheere.repository.bus.query.BusNumDirDTO;
import com.d138.wheere.repository.bus.query.BusQueryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class BusRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    BusRepository busRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    BusQueryRepository busQueryRepository;

    @Test
    public void 버스번호_방향_조회_테스트() {

        // Given
        Bus bus7 = new Bus(7L, 1, BusState.FORWARD, "340", LocalTime.of(8, 0, 0));
        Bus bus8 = new Bus(8L, 2, BusState.FORWARD, "340", LocalTime.of(9, 0, 0));
        Bus bus9 = new Bus(9L, 3, BusState.FORWARD, "340", LocalTime.of(10, 0, 0));

        Bus bus10 = new Bus(10L, 1, BusState.REVERSED, "340", LocalTime.of(8, 30, 0));
        Bus bus11 = new Bus(11L, 2, BusState.REVERSED, "340", LocalTime.of(9, 30, 0));
        Bus bus12 = new Bus(12L, 3, BusState.REVERSED, "340", LocalTime.of(10, 30, 0));

        Bus bus4 = new Bus(4L, 1, BusState.REVERSED, "191", LocalTime.of(8, 30, 0));
        Bus bus5 = new Bus(5L, 2, BusState.REVERSED, "191", LocalTime.of(9, 30, 0));
        Bus bus6 = new Bus(6L, 3, BusState.REVERSED, "191", LocalTime.of(10, 30, 0));

        Bus bus1 = new Bus(1L, 1, BusState.FORWARD, "191", LocalTime.of(8, 0, 0));
        Bus bus2 = new Bus(2L, 2, BusState.FORWARD, "191", LocalTime.of(9, 0, 0));
        Bus bus3 = new Bus(3L, 3, BusState.FORWARD, "191", LocalTime.of(10, 0, 0));


        em.persist(bus7);
        em.persist(bus8);
        em.persist(bus9);
        em.persist(bus10);
        em.persist(bus11);
        em.persist(bus12);
        em.persist(bus4);
        em.persist(bus5);
        em.persist(bus6);
        em.persist(bus1);
        em.persist(bus2);
        em.persist(bus3);

        // When
        em.flush();
        em.clear();

        List<BusNumDirDTO> busNumDir = busQueryRepository.findBusNumDir();

        // Then
        for (BusNumDirDTO busNumDirDTO : busNumDir) {
            System.out.println("busNumDirDTO = " + busNumDirDTO.getBNumber());
            System.out.println("busNumDirDTO.getDir() = " + busNumDirDTO.getBDir());
        }
    }

    @Test
    public void 버스_출발시간_조회_테스트() {

        // Given
        Bus bus1 = new Bus(1L, 1, BusState.FORWARD, "191", LocalTime.of(8, 0, 0));
        Bus bus2 = new Bus(2L, 2, BusState.FORWARD, "191", LocalTime.of(9, 0, 0));
        Bus bus3 = new Bus(3L, 3, BusState.FORWARD, "191", LocalTime.of(10, 0, 0));
        em.persist(bus1);
        em.persist(bus2);
        em.persist(bus3);

        // When
        em.flush();
        em.clear();

        List<LocalTime> departureTimes = busRepository.findDepartureTime("191", BusState.FORWARD);

        // Then
        for (LocalTime departureTime : departureTimes) {
            System.out.println("departureTime = " + departureTime);
        }
    }
}
