package com.d138.wheere.controller.memberDTO;

import com.d138.wheere.domain.BusState;
import lombok.Data;

@Data
public class CheckBusDTO {
    private String bNumber;
    private BusState bDir;
}
