package com.d138.wheere.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserLoginRequestDto {
    @NotNull(message = "이름은 Null 일 수 없습니다!")
    @Size(min = 1, max = 10, message = "이름은 1 ~ 10자 이여야 합니다!")
    private String name;

    @NotNull(message = "나이는 Null 일 수 없습니다.")
    private int age;

    @NotNull(message = "전화번호는 Null 일 수 없습니다!")
    @Size(min = 11, max = 11, message = "전화번호는 11자리 이여야 합니다!")
    private String phoneNumber;
}
