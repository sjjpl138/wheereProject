package com.d138.wheere.service;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.Driver;
import com.d138.wheere.domain.Member;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    private final BusRepository busRepository;

    @Transactional
    public String join(Driver driver) {
        driverRepository.save(driver);

        return driver.getId();
    }

    @Transactional
    public void reflectScores (Long busId, double score) {

        // 사용자의 예약 상태가 완료 (COMP)인 경우에만 가능하도록
        // 아니면 예외 발생

        Driver findDriver = findDriverByBus(busId);

        findDriver.calculateRatings(score);
    }

    @Transactional
    public void changeBus(String driverId, Bus bus) {
        Driver findDriver = driverRepository.findOne(driverId);
        Bus findBus = busRepository.findOne(bus.getId());
        findDriver.changeBus(findBus);
    }

    // 평점 확인
    public double checkRating(String driverId) {
        Driver findDriver = driverRepository.findOne(driverId);
        return findDriver.getRatingScore();
    }

    public Driver findDriver(String driverId) {
        return driverRepository.findOne(driverId);
    }

    public Driver findDriverByBus(Long busId) {
        return driverRepository.findByBusId(busId);
    }
}
