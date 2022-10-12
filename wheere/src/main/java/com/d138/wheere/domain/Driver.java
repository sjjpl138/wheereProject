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
public class Driver {

    @Id
    @GeneratedValue
    @Column(name = "DRIVER_ID")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private double ratingScore;

    @NotNull
    private int ratingCnt;
}
