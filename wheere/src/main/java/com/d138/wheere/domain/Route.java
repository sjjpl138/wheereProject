package com.d138.wheere.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
public class Route {

    @Id
    @GeneratedValue
    @Column(name = "ROUTE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUS_ID")
    private Bus bus;

    @NotNull
    private String station;

    @NotNull
    private int stationSeq;
}
