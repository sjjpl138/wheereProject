package com.d138.wheere.service;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.Driver;
import com.d138.wheere.repository.DriverRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class DriverServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private DriverService driverService;

    @Autowired
    private DriverRepository driverRepository;

    @Test
    public void 가입_조회() {

        // Given
        Driver driver = new Driver();
        driver.setId("1L");
        driver.setRatingScore(0);
        driver.setBus(null);
        driver.setRatingCnt(0);
        driver.setName("정영한");

        // When
        driverService.join(driver);

        em.flush();
        em.clear();

        // Then
        Driver findDriver = driverService.findDriver(driver.getId());

        assertThat(findDriver.getId()).isEqualTo("1L");
        assertThat(findDriver.getName()).isEqualTo("정영한");
        assertThat(findDriver.getRatingCnt()).isEqualTo(0);
        assertThat(findDriver.getRatingScore()).isEqualTo(0);
        assertThat(findDriver).isInstanceOf(Driver.class);
    }
    
    @Test
    public void 버스변경() {
        
        // Given
        Bus bus1 = new Bus();
        bus1.setId(1L);

        Bus bus2 = new Bus();
        bus2.setId(2L);

        Driver driver = new Driver();
        driver.setId("1L");
        driver.setRatingScore(0);
        driver.setBus(bus1);
        driver.setRatingCnt(0);
        driver.setName("정영한");

        // When
        em.persist(bus1);
        em.persist(bus2);
        driverService.join(driver);

        em.flush();
        em.clear();

        // Then
        Driver findDriver = driverService.findDriver(driver.getId());
        System.out.println("findDriver.getBus().getId() = " + findDriver.getBus().getId());
        assertThat(findDriver.getBus().getId()).isEqualTo(1L);

        driverService.changeBus(driver.getId(), 2L);

        System.out.println("findDriver.getBus().getId() = " + findDriver.getBus().getId());
        assertThat(findDriver.getBus().getId()).isEqualTo(2L);
        assertThat(findDriver.getBus()).isInstanceOf(Bus.class);
    }

    @Test
    public void 평점_반영() {

        // Given
        Bus bus1 = new Bus();
        bus1.setId(1L);
        em.persist(bus1);

        Driver driver = new Driver();
        driver.setId("1L");
        driver.setRatingScore(0);
        driver.setBus(bus1);
        driver.setRatingCnt(0);
        driver.setName("정영한");
        em.persist(driver);

        // When
        driverService.reflectScores(1L, 4.0);
        driverService.reflectScores(1L, 5.0);
        driverService.reflectScores(1L, 3.0);

        em.flush();
        em.clear();

        Driver findDriver = driverService.findDriver(driver.getId());

        // Then
        assertThat(findDriver.getRatingScore()).isEqualTo(4.0);

    }
}
