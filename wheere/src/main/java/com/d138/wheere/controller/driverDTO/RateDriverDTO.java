package com.d138.wheere.controller.driverDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class RateDriverDTO {
    private Long bId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rDate;
}
