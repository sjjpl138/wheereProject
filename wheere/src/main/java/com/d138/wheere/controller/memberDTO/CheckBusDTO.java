package com.d138.wheere.controller.memberDTO;

import com.d138.wheere.domain.BusState;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckBusDTO {
    private String bNumber;
    private BusState bDir;
}
