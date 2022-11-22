package com.d138.wheere.service;

import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Route;
import com.d138.wheere.repository.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
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
    private List<Route> findBusRouteByBusInfo(String busNum, BusState busState) {
        return routeRepository.findBusRouteByBusInfo(busNum, busState);
    }

    /**
     * 8. 버스 시간표 조회 /user/bus - GET 에서 호출됨
     * @param busNum
     * @param direction
     * @param startSeq
     * @param endSeq
     * @return
     */
    public List<Object[]> inquiryBusSchedule(String busNum, BusState direction, int startSeq, int endSeq) {
        List<Integer> seqList = Arrays.asList(startSeq, endSeq);
        return routeRepository.inquiryBusSchedule(busNum, direction, seqList);
    }
}
