package com.d138.wheere.controller.driverDTO;

import com.d138.wheere.domain.BusState;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AssignBusDTO {
    private String dId;
    private Long bId;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime bOperationDate;
}
