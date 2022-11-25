package com.d138.wheere.service;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusState;
import com.d138.wheere.repository.bus.query.BusNumDirDTO;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.bus.query.BusQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    private final BusQueryRepository busQueryRepository;

    // 사용자 또는 버스 기사가 버스 번호, 방향, 출발 시간으로 버스 선택
    // 버스 예약, 버스 배차에 사용됨
    public Bus selectBus(String busNum, BusState busState, LocalTime departureTime) {
        return busRepository.findBus(busNum, busState, departureTime);
    }

    // TODO (BusID로 버스 조회할 필요가 없는가?)


    /**
     * 버스 번호, 방향만 조회하는 메서드
     * 버스 기사가 로그인 후 자신의 버스 배차를 변경하고자 할 때 조회용으로 사용
     *
     * @return 버스 번호와 방향 List
     */
    public List<BusNumDirDTO> inquireBusNumDir() {
        return busQueryRepository.findBusNumDir();
    }

    // 버스 시간표 조회 메서드
    public List<Bus> inquireBusDepartureTime(String busNum, BusState direction) {
        return busRepository.findDepartureTime(busNum, direction);
    }

    /**
     * 사용자 8. 버스 시간표 조회에서 사용됨
     * 버스 번호 조회
     * @param busNum
     * @param direction
     * @return
     */
    public List<Long> inquireBusIdByBusNumAndDirection(String busNum, BusState direction) {
        return busRepository.findBusIdByBusNumAndDirection(busNum, direction);
    }
}
