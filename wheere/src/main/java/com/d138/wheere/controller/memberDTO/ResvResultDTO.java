package com.d138.wheere.controller.memberDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResvResultDTO {
    private Long uid;
    private Long rid;
    private LocalDateTime rtime;
    private String rstart;
    private String rend;
    private boolean risPaid;
    private String bnumber;
}
