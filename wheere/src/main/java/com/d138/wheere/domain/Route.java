package com.d138.wheere.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Route {

    @Id
    @Column(name = "ROUTE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUS_ID")
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATION_ID")
    private Station station;

    // 정류장 순서
    private int stationSeq;

    // 예상 도착 시간 (from 이전 정류장)
    private LocalTime arrivalTime;

    // 예약 가능한 남은 좌석 수
    private int leftSeatsNum;
}
