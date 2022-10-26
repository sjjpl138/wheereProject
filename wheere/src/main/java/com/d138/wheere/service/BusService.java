package com.d138.wheere.service;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.repository.BusNumDirDTO;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.ReservationRepository;
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

    private final ReservationRepository reservationRepository;

    // 사용자 또는 버스 기사가 버스 번호, 방향, 출발 시간으로 버스 선택
    // 버스 예약, 버스 배차에 사용됨
    public Bus selectBus(String busNum, BusState busState, LocalTime departureTime) {
        return busRepository.findBus(busNum, busState, departureTime);
    }

    // 버스 번호, 방향만 조회하는 메서드
    public List<BusNumDirDTO> inquireBusNumDir() {
        return busRepository.findBusNumDir();
    }

    // 버스 시간표 조회 메서드
    public List<LocalTime> inquireBusDepartureTime(String busNum, BusState direction) {
        return busRepository.findDepartureTime(busNum, direction);
    }
}
