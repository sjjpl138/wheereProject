package com.d138.wheere.domain;

import com.d138.wheere.exception.NotEnoughSeatsException;
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

    public Route(Long id, Bus bus, Station station, int stationSeq, LocalTime arrivalTime) {
        this.id = id;
        setBus(bus);
        this.station = station;
        this.stationSeq = stationSeq;
        this.arrivalTime = arrivalTime;
    }

    /* 연관관계 편의 메서드 */
    public void setBus(Bus bus) {
        if (this.bus != null) {
            this.bus.getRoutes().remove(this);
        }
        this.bus = bus;
        bus.getRoutes().add(this);
    }
}
