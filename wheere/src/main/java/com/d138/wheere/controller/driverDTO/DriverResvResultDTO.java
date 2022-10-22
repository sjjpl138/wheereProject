package com.d138.wheere.controller.driverDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DriverResvResultDTO {
    private Long rid;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rtime;
    private String rstart;
    private String rend;
    private String rIsPaid;

    private String uid;
    private String uname;
    private int uage;
    private String usex;
    private String uphonenumber;

    public DriverResvResultDTO(Long rid, LocalDate rtime, String rstart, String rend, String rIsPaid, String uid, String uname, int uage, String usex, String uphonenumber) {
        this.rid = rid;
        this.rtime = rtime;
        this.rstart = rstart;
        this.rend = rend;
        this.rIsPaid = rIsPaid;
        this.uid = uid;
        this.uname = uname;
        this.uage = uage;
        this.usex = usex;
        this.uphonenumber = uphonenumber;
    }
}
