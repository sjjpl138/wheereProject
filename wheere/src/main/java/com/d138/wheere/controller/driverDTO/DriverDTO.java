package com.d138.wheere.controller.driverDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DriverDTO {
    private String did;
    private String dname;
    private String bnumber;

    public DriverDTO(String did, String dname, String bnumber) {
        this.did = did;
        this.dname = dname;
        this.bnumber = bnumber;
    }
}
