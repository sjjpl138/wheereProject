package com.d138.wheere.service;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Reservation;
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
    // 이 메서드로 버스 예약, 버스 배차
    public Bus selectBus(String busNum, BusState busState, LocalTime departureTime) {
        return busRepository.findBus(busNum, busState, departureTime);
    }
}
