package com.d138.wheere.controller.driverDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DriverResvResultDTO {
    private Long rid;
    private LocalDateTime rtime;
    private String rstart;
    private String rend;
    private String rIsPaid;

    private Long uid;
    private String uname;
    private int uage;
    private String usex;
    private String uphonenumber;

    public DriverResvResultDTO(Long rid, LocalDateTime rtime, String rstart, String rend, String rIsPaid, Long uid, String uname, int uage, String usex, String uphonenumber) {
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
