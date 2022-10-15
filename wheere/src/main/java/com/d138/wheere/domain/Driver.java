package com.d138.wheere.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Driver {

    @Id
    @GeneratedValue
    @Column(name = "DRIVER_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUS_ID")
    private Bus bus;

    @NotNull
    private String name;

    @NotNull
    private double ratingScore;

    @NotNull
    private int ratingCnt;
}
