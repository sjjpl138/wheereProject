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
    @Column(name = "DRIVER_ID")
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUS_ID")
    private Bus bus;

    private String name;

    private double ratingScore;

    private int ratingCnt;

    /* 비지니스 로직 */
    public void changeBus(Bus bus) {
        this.bus = bus;
    }
}
