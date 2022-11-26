package com.d138.wheere.service;

import com.d138.wheere.domain.*;
import com.d138.wheere.repository.BusDriverRepository;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
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
     * 버스 기사 평점 반영할 때 호출됨
     * 버스 ID (PK), 운행 날짜, 평점 필요
     *
     * @param busId         버스 ID (PK)
     * @param operationDate 운행 날짜
     * @param score         평점
     * @return 반영 후 평점
     */
    @Transactional
    public double reflectScores(Long busId, LocalDate operationDate, double score) {

        BusDriver findBusDriver = busDriverRepository.findBusDriverWithDriver(busId, operationDate);
        Driver findDriver = findBusDriver.getDriver();

        findDriver.calculateRatings(score);

        return findDriver.getRatingScore();
    }

    /**
     * 버스 기사 버스 배정
     * 예외처리 필요
     * [운행 날짜], [버스 번호, 방향, 출발 시간], [버스기사 ID]를 받아와 해당 버스를 배정한다.
     *
     * @param operationDate 운행 날짜
     * @param busId         버스 ID (PK)
     * @param driverId      버스기사 ID (PK)
     * @return 생성된 BusDriver ID (PK)
     */
    @Transactional
    public Long selectBus(LocalDate operationDate, Long busId, String driverId) {

        Bus findBus = busRepository.findOne(busId);

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
     * 버스 배차 취소
     *
     * @param driverId
     */
    @Transactional
    public void cancelBus(String driverId, Long busId) throws NoResultException {

        BusDriver findBusDriver = busDriverRepository.findBusDriverByDriverAndDate(driverId, busId, LocalDate.now());
        busDriverRepository.delete(findBusDriver);
    }

    /**
     * 배차된 버스 운행 완료
     *
     * @param driverId
     */
    @Transactional
    public void completeBus(String driverId, Long busId) throws NoResultException {

        BusDriver findBusDriver = busDriverRepository.findBusDriverByDriverAndDate(driverId, busId, LocalDate.now());
        findBusDriver.complete();
    }

    /**
     * 버스기사 평점 조회 시 호출됨
     *
     * @param driverId
     * @return 버스기사 평점
     */
    public double checkRating(String driverId) {
        Driver findDriver = driverRepository.findOne(driverId);
        return findDriver.getRatingScore();
    }

    /**
     * 버스기사 ID로 버스기사 조회 시 사용
     *
     * @param driverId
     * @return
     */
    public Driver findOne(String driverId) {
        return driverRepository.findOne(driverId);
    }

}
