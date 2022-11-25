package com.d138.wheere.service;

import com.d138.wheere.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    /**
     * 8. 버스 시간표 조회 /user/bus - GET 에서 호출됨
     * @param busId 버스 ID (PK)
     * @param reservationDate 예약 날짜
     * @param startSeq 승차 정류장 순번
     * @param endSeq 하차 정류장 순번
     * @return 예약 가능한 남은 좌석 수
     */
    public List<Integer> inquireMinLeftSeatNum(Long busId, LocalDate reservationDate, int startSeq, int endSeq) {
        List<Integer> seqList = new ArrayList<>();
        for(int i = startSeq; i < endSeq; i++)
            seqList.add(i);

        return seatRepository.inquiryMinLeftSeatNum(busId, reservationDate, seqList);
    }
}
