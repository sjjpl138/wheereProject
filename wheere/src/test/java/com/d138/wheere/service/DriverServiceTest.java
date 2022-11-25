package com.d138.wheere.service;

import com.d138.wheere.domain.*;
import com.d138.wheere.repository.BusDriverRepository;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @Autowired
    BusRepository busRepository;

    @Autowired
    private BusDriverRepository busDriverRepository;

    private Long mId = 0L;
    private Long bId = 0L;

    @Test
    public void 버스_배정_테스트() {

        // Given
        Driver driver = new Driver("1L", "홍길동", 0, 0);
        em.persist(driver);
        Long busId = createBus("191-5");
        Bus findBus = busRepository.findOne(busId);

        String busNumber = findBus.getBusNumber();
        BusState direction = findBus.getDirection();
        LocalTime departureTime = findBus.getDepartureTime();

        LocalDate date = LocalDate.of(2022, 1, 1);

        // When
        Long busDriverId = driverService.selectBus(date, busId, driver.getId());

        em.flush();
        em.clear();

        // Then
        BusDriver findBusDriver = busDriverRepository.findBusDriverWithDriver(findBus.getId(), date);

        assertThat(findBusDriver.getDriver().getName()).isEqualTo("홍길동");
    }

    @Test
    public void 평점_반영_테스트() {

        // Given
        Driver driver = new Driver("1L", "홍길동", 0, 0);
        em.persist(driver);
        Long busId = createBus("191-5");
        Bus findBus = busRepository.findOne(busId);

        String busNumber = findBus.getBusNumber();
        BusState direction = findBus.getDirection();
        LocalTime departureTime = findBus.getDepartureTime();

        LocalDate date = LocalDate.of(2022, 1, 1);

        Long busDriverId = driverService.selectBus(date, busId, driver.getId());

        em.flush();
        em.clear();

        // When
        double score1 = driverService.reflectScores(busId, date, 4.0);
        double score2 = driverService.reflectScores(busId, date, 3.9);
        double score3 = driverService.reflectScores(busId, date, 5.0);

        em.flush();
        em.clear();

        // Then
        assertThat(score1).isEqualTo(4.0);
        assertThat(score3).isEqualTo(4.3);

        Driver findDriver = driverRepository.findOne(driver.getId());
        assertThat(findDriver.getRatingScore()).isEqualTo(4.3);
        assertThat(findDriver.getRatingCnt()).isEqualTo(3);

        double driverScore = driverService.checkRating(driver.getId());
        assertThat(driverScore).isEqualTo(4.3);
    }

    private String createMember(String name) {
        Member member = new Member();
        member.setId((mId++).toString());
        member.setName(name);
        member.setBirthDate(LocalDate.of(1999, 03, 02));
        member.setPhoneNumber("010-1111-1111");
        member.setSex("female");

        em.persist(member);
        return member.getId();
    }

    private Long createBus(String busNum) {
        Bus bus = new Bus();
        bus.setId(bId++);
        bus.setBusNumber(busNum);
        bus.setDirection(BusState.FORWARD);
        bus.setBusAllocationSeq(1);
        bus.setDepartureTime(LocalTime.now().plusMinutes(10));
        bus.setRoutes(null);
        em.persist(bus);

        return bus.getId();
    }
}
