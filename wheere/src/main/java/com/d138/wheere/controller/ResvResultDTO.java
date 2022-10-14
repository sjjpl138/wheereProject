package com.d138.wheere.controller;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResvResultDTO {
    private Long uid;
    private Long rid;
    private LocalDateTime rTime;
    private String rStart;
    private String rEnd;
    private boolean rIsPaid;
    private String bNumber;
}
