package com.d138.wheere.controller.memberDTO;

import com.d138.wheere.domain.Reservation;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ResvResultDTO {
    private String  uid;
    private Long rid;
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

    public static  ResvResultDTO createResResult(String uid, boolean isPaid, Reservation reservation) {
        return new ResvResultDTO(uid, reservation.getId(), reservation.getReservationDate(), reservation.getStartPoint(), reservation.getEndPoint(), isPaid, reservation.getBus().getBusNumber());
    }
}
