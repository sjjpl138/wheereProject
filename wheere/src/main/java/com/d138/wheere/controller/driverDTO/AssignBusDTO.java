package com.d138.wheere.controller.driverDTO;

import com.d138.wheere.domain.BusState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
public class AssignBusDTO {
    private String bNumber;
    private BusState bDir;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime bStartTime;
}
