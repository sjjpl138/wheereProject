package com.d138.wheere.controller.driverDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverCancelResultDTO {
    private Long bid;
    private Long rid;
    private boolean idCancelled;

    public DriverCancelResultDTO(Long bid, Long rid, boolean idCancelled) {
        this.bid = bid;
        this.rid = rid;
        this.idCancelled = idCancelled;
    }
}
