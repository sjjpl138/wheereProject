package com.d138.wheere.controller.memberDTO;

import com.d138.wheere.domain.BusState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckBusTimeDTO {
    private String bNumber;
    private BusState bDir;
}
