package com.d138.wheere.controller.driverDTO;

import lombok.Getter;

@Getter
public class DriverDTO {
    private Long did;
    private String dname;
    private String bnumber;

    public DriverDTO(Long did, String dname, String bnumber) {
        this.did = did;
        this.dname = dname;
        this.bnumber = bnumber;
    }
}
