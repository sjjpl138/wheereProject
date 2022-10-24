package com.d138.wheere.domain;

import com.d138.wheere.exception.NotEnoughSeatsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue
    @Column(name = "SEAT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUS_ID")
    private Bus bus;

    // 버스 운행 날짜
    LocalDate operationDate;

    // 전체 교통 약자 좌석 수
    private int totalWheelChairSeats;

    // 남은 교통 약자 좌석 수
    private int leftWheelChairSeats;

    public Seat(Bus bus, LocalDate operationDate, int totalWheelChairSeats) {
        this.bus = bus;
        this.operationDate = operationDate;
        this.totalWheelChairSeats = totalWheelChairSeats;
        this.leftWheelChairSeats = totalWheelChairSeats;
    }

    /* 생성 메서드 */
    public static Seat createSeat(Bus bus, LocalDate operationDate, int totalWheelChairSeats) {
        return new Seat(bus, operationDate, totalWheelChairSeats);
    }

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
