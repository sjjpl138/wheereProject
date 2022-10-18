package com.d138.wheere.domain;

import com.d138.wheere.exception.NotEnoughSeatsException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Bus {

    @Id
    @Column(name = "BUS_ID")
    private Long id;

    @OneToMany(mappedBy = "bus")
    private List<Route> routes = new ArrayList<>();

    private int busAllocationSeq;

    @Enumerated(EnumType.STRING)
    private BusState direction;

    private String busNumber;

    private int totalWheelChairSeats;

    private int leftWheelChairSeats;

    /* 비지니스 로직 */
    public void addSeats() {
        int restSeats = this.leftWheelChairSeats + 1;
        if (restSeats <= totalWheelChairSeats) {
            this.leftWheelChairSeats = restSeats;
        }
    }

    public void subSeats() {
        int restSeats = this.leftWheelChairSeats - 1;
        if (restSeats < 0) {
            throw new NotEnoughSeatsException("남은 좌석이 없습니다.");
        }
        this.leftWheelChairSeats = restSeats;
    }
}
