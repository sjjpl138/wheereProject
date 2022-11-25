package com.d138.wheere.domain;

import com.d138.wheere.exception.NotEnoughSeatsException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTE_ID")
    private Route route;

    // 예약 가능한 총 좌석 수
    private int totalSeatsNum;

    // 예약 가능한 남은 좌석 수
    private int leftSeatsNum;

    // 날짜
    private LocalDate reservationDate;

    public Seat(Route route, int totalSeatsNum, int leftSeatsNum, LocalDate reservationDate) {
        this.route = route;
        this.totalSeatsNum = totalSeatsNum;
        this.leftSeatsNum = leftSeatsNum;
        this.reservationDate = reservationDate;
    }

    /* 생성 메서드 */
    public static Seat createSeat(Route route, int totalSeatsNum, LocalDate reservationDate) {
        return new Seat(route, totalSeatsNum, totalSeatsNum, reservationDate);
    }

    /* 비지니스 로직 */
    public void addSeats() {
        int restSeats = this.leftSeatsNum + 1;
        if (restSeats <= totalSeatsNum) {
            this.leftSeatsNum = restSeats;
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
