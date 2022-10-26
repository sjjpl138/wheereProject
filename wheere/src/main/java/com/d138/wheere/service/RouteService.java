package com.d138.wheere.service;

import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Route;
import com.d138.wheere.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    // 버스 번호와 버스 방향으로 버스 노선 조회
    private List<Route> findBusRouteByBusInfo(String busNum, BusState busState) {
        return routeRepository.findBusRouteByBusInfo(busNum, busState);
    }
}
