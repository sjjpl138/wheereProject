package com.d138.wheere.domain;

import com.d138.wheere.exception.NotEnoughSeatsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Bus {

    @Id
    @Column(name = "BUS_ID")
    private Long id;

    // 버스 경로
    @OneToMany(mappedBy = "bus", cascade = CascadeType.REMOVE)
    private List<Route> routes = new ArrayList<>();

    // 버스 배차 순번
    private int busAllocationSeq;

    // 버스 방향 (정방향, 역방향)
    @Enumerated(EnumType.STRING)
    private BusState direction;

    // 버스 번호
    private String busNumber;

    // 버스 출발 시간
    @DateTimeFormat(pattern = "hh:mm:ss")
    private LocalTime departureTime;

    public Bus(Long id, int busAllocationSeq, BusState direction, String busNumber, LocalTime departureTime) {
        this.id = id;
        this.busAllocationSeq = busAllocationSeq;
        this.direction = direction;
        this.busNumber = busNumber;
        this.departureTime = departureTime;
    }
}
