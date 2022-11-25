package com.d138.wheere.controller.memberDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReservationDTO {
    private String uId;
    private  Long bId;
    private int startSeq;
    private int endSeq;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rTime;

}
