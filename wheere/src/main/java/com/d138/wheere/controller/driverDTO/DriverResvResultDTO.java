package com.d138.wheere.controller.driverDTO;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class DriverResvResultDTO {
    private Long rId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate rTime;
    private String rStart;
    private String rEnd;
    private String rIsPaid;

    private String uId;
    private String uName;
    private int uAge;
    private String uSex;
    private String uNum;

}
