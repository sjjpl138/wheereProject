package com.d138.wheere.service;

import com.d138.wheere.domain.BusDriver;
import com.d138.wheere.domain.Driver;
import com.d138.wheere.repository.BusDriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BusDriverService {
    private final BusDriverRepository busDriverRepository;

    /**
     * 사용자 예약하기에서 사용
     * SSE 데이터 전송하기 위해 사용
     * 버스 id와 운행 날짜로 기사 조회하는 메서드
     * @param bId
     * @param operationDate
     * @return
     */
    public List<BusDriver> findDriverByBId(Long bId, LocalDate operationDate) {
        return busDriverRepository.findBusDriverListWithDriver(bId, operationDate);
    }
}
