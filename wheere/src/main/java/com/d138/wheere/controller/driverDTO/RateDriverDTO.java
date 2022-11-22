package com.d138.wheere.controller.driverDTO;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RateDriverDTO {
    private Long bId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rDate;
}
