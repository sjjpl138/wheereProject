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

//    @Transactional
//    public void calculatingRatings() {
//
//    }

    @Transactional
    public void changeBus(String driverId, Long busId) {
        Driver findDriver = driverRepository.findOne(driverId);
        Bus findBus = busRepository.findOne(busId);
        findDriver.changeBus(findBus);
    }

    public Driver findDriver(String driverId) {
        return driverRepository.findOne(driverId);
    }

    /*public List<Driver> findDriverByBus(Long busId) {
        return driverRepository.findByBusId(busId);
    }*/
}
