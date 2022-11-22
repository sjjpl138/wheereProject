package com.d138.wheere.service;

import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Route;
import com.d138.wheere.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    /**
     * 버스 번호와 버스 방향으로 버스 노선 조회
     * @param busNum
     * @param busState
     * @return
     */
    public List<Route> findBusRouteByBusInfo(String busNum, BusState busState) {
        return routeRepository.findBusRouteByBusInfo(busNum, busState);
    }

    /**
     * 사용자 8. 버스 시간표 조회에서 호출됨
     * @param busId 버스 ID (PK)
     * @param stationSeq 정류장 순번
     * @return 해당 정류장에 버스가 도착하는 시간
     */
    public LocalTime inquireTimeByBusAndSeq(Long busId, int stationSeq) {
        return routeRepository.findTimeByBusAndSeq(busId, stationSeq);
    }
}
