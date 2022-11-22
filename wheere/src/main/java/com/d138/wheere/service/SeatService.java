package com.d138.wheere.service;

import com.d138.wheere.domain.BusState;
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
     * @param busNum
     * @param busState
     * @param reservationDate
     * @param startSeq
     * @param endSeq
     * @return
     */
    public int inquiryMinLeftSeatNum(String busNum, BusState busState, LocalDate reservationDate, int startSeq, int endSeq) {
        List<Integer> seqList = new ArrayList<>();
        for(int i = startSeq; i <= endSeq; i++)
            seqList.add(i);

        return seatRepository.inquiryMinLeftSeatNum(busNum, busState, reservationDate, seqList);
    }
}
