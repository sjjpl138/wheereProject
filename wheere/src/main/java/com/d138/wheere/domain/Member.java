package com.d138.wheere.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    // 버스 요금 구분을 위한 나이
    private int age;

    @NotNull
    private String phoneNumber;
}
