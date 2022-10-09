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
public class Bus {

    @Id
    @GeneratedValue
    @Column(name = "BUS_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DRIVER_ID")
    private Driver driver;

    @OneToMany(mappedBy = "bus")
    private List<Route> routes = new ArrayList<>();

    @NotNull
    private String busNumber;

    @NotNull
    private int totalWheelChairSeats;

    @NotNull
    private int leftWheelChairSeats;

    /* 비지니스 로직 */
    public void addSeats(int capacity) {
        if (this.leftWheelChairSeats + capacity <= totalWheelChairSeats) {
            this.leftWheelChairSeats += capacity;
        }
    }

    public void subSeats(int capacity) {
        int restSeats = this.leftWheelChairSeats - capacity;
        if (restSeats < 0) {
            throw new NotEnoughSeatsException("남은 좌석이 없습니다.")
        }
        this.leftWheelChairSeats = restSeats;
    }
}
