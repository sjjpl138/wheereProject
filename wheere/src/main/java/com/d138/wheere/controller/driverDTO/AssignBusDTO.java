package com.d138.wheere.controller.driverDTO;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AssignBusDTO {
    private String dId;
    private Long bId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bOperationDate;
}
