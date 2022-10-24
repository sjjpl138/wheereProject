package com.d138.wheere.controller.memberDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class ReservationDTO {
    private String uId;
    private  Long bId;
    private String rStart;
    private String rEnd;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rDate;

}
