package com.d138.wheere.controller.memberDTO;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ReservationDTO {
    private String uId;
    private  Long bId;
    private String rStart;
    private String rEnd;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rDate;

}
