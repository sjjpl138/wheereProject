package com.d138.wheere.controller.memberDTO;

import com.d138.wheere.domain.Reservation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ResvResultDTO {
    private String  uid;
    private Long rid;
    @JsonFormat(shape = JsonFormat.Shape.STRING,  pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rTime;

    private String rStart;
    private String rEnd;
    private boolean rIsPaid;
    private String bNumber;

    public ResvResultDTO(String uid, Long rid, LocalDate rTime, String rStart, String rEnd, boolean rIsPaid, String bNumber) {
        this.uid = uid;
        this.rid = rid;
        this.rTime = rTime;
        this.rStart = rStart;
        this.rEnd = rEnd;
        this.rIsPaid = rIsPaid;
        this.bNumber = bNumber;
    }

    public static  ResvResultDTO createResResult(String uId, boolean isPaid, Reservation resv) {
        return new ResvResultDTO(uId, resv.getId(), resv.getReservationDate(), resv.getStartPoint(), resv.getEndPoint(), isPaid, resv.getBus().getBusNumber());
    }
}
