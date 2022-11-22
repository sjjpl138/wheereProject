package com.d138.wheere.controller.memberDTO;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class MemberDTO {
    private String uId;
    private String uName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate uBirthDate;
    private String  uSex;
    private String uNum;

}
