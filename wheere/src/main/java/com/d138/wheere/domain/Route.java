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

    // 예약 가능한 총 좌석 수
    private int totalSeatsNum;

    // 예약 가능한 남은 좌석 수
    private int leftSeatsNum;

    public Route(Long id, Bus bus, Station station, int stationSeq, LocalTime arrivalTime, int totalSeatsNum, int leftSeatsNum) {
        this.id = id;
        setBus(bus);
        this.station = station;
        this.stationSeq = stationSeq;
        this.arrivalTime = arrivalTime;
        this.totalSeatsNum = totalSeatsNum;
        this.leftSeatsNum = leftSeatsNum;
    }

    /* 연관관계 편의 메서드 */
    public void setBus(Bus bus) {
        if (this.bus != null) {
            this.bus.getRoutes().remove(this);
        }
        this.bus = bus;
        bus.getRoutes().add(this);
    }

    /* 비지니스 로직 */
    public void addSeats() {
        int restSeats = this.leftSeatsNum + 1;
        if (restSeats <= totalSeatsNum) {
            this.totalSeatsNum = restSeats;
        }
    }

    public void subSeats() {
        int restSeats = this.leftSeatsNum - 1;
        if (restSeats < 0) {
            throw new NotEnoughSeatsException("남은 좌석이 없습니다.");
        }
        this.leftSeatsNum = restSeats;
    }
}
