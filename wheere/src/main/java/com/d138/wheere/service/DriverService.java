package com.d138.wheere.service;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusDriver;
import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Driver;
import com.d138.wheere.repository.BusDriverRepository;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    private final BusRepository busRepository;

    private final BusDriverRepository busDriverRepository;

    /**
     * 버스 기사 회원 가입 시 사용
     * @param driver
     * @return 버스기사ID (PK)
     */
    @Transactional
    public String join(Driver driver) {
        driverRepository.save(driver);

        return driver.getId();
    }

    /**
     * 버스 기사 평점 반영할 때 호출됨
     * 버스 ID (PK), 운행 날짜, 평점 필요
     * @param busId 버스 ID (PK)
     * @param operationDate 운행 날짜
     * @param score 평점
     * @return 반영 후 평점
     */
    @Transactional
    public double reflectScores (Long busId, LocalDate operationDate, double score) {

        // TODO (버스 기사 평점 남기기 구현)
        // 사용자의 예약 상태가 완료 (COMP)인 경우에만 가능하도록-> 파라미터로 사용자 ID나 예약 ID 추가로 받아야 할 듯
        // 아니면 예외 발생

        BusDriver findBusDriver = busDriverRepository.findBusDriverWithDriver(busId, operationDate);
        Driver findDriver = findBusDriver.getDriver();

        findDriver.calculateRatings(score);

        return findDriver.getRatingScore();
    }

    /**
     * 버스 기사 버스 배정
     * 예외처리 필요
     * [운행 날짜], [버스 번호, 방향, 출발 시간], [버스기사 ID]를 받아와 해당 버스를 배정한다.
     * @param operationDate 운행 날짜
     * @param busNum 버스 번호
     * @param busState 버스 방향 (정방향, 역방향)
     * @param departureTime 출발 시간
     * @param driverId 버스기사 ID (PK)
     * @return 생성된 BusDriver ID (PK)
     */
    @Transactional
    public Long selectBus(LocalDate operationDate, String busNum, BusState busState, LocalTime departureTime, String driverId) {

        Bus findBus = busRepository.findBus(busNum, busState, departureTime);

        Driver findDriver = driverRepository.findOne(driverId);

        // 동일 날짜 동일 버스에 이미 버스가 배차되어 있으면 예외 발생
        validateDuplicateBusDispatch(findBus.getId(), operationDate);

        // BusDriver 생성
        BusDriver busDriver = BusDriver.createBusDriver(findBus, findDriver, operationDate);
        Long busDriverId = busDriverRepository.save(busDriver);

        return busDriverId;
    }

    // 중복된 버스 배차가 있는지 검사
    private void validateDuplicateBusDispatch(Long busId, LocalDate operationDate) {
        List<BusDriver> busDrivers = busDriverRepository.findBusDriverByBusAndDate(busId, operationDate);

        if (!busDrivers.isEmpty()) {
            throw new IllegalStateException("이미 배정된 버스입니다.");
        }
    }

    /**
     * 버스기사 평점 조회 시 호출됨
     * @param driverId
     * @return 버스기사 평점
     */
    public double checkRating(String driverId) {
        Driver findDriver = driverRepository.findOne(driverId);
        return findDriver.getRatingScore();
    }

    /**
     * 버스기사 ID로 버스기사 조회 시 사용
     * @param driverId
     * @return
     */
    public Driver findOne(String driverId) {
        return driverRepository.findOne(driverId);
    }

}
