package com.d138.wheere.controller.memberDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class MemberDTO {
    private String uName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate uBirthDate;
    private String  uSex;
    private String uNum;

}
