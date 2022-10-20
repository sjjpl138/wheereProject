package com.d138.wheere.controller.driverDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DriverCancelResultDTO {
    private Long bid;
    private String rid;
    private boolean idCancelled;

}
