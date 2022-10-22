package com.d138.wheere.controller.memberDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class ReservationDTO {
    private Long rid;
    private String uid;
    private  Long bid;
    private String startpoint;
    private String endpoint;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rdate;

    public ReservationDTO(Long rid, String uid, Long bid, String startpoint, String endpoint, LocalDate rdate) {
        this.rid = rid;
        this.uid = uid;
        this.bid = bid;
        this.startpoint = startpoint;
        this.endpoint = endpoint;
        this.rdate = rdate;
    }
}
