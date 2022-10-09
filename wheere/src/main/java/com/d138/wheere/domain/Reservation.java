package com.d138.wheere.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "RESERVATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUIS_ID")
    private Bus bus;

    @NotNull
    private String startPoint;

    @NotNull
    private String endPoint;

    @NotNull
    private LocalDateTime reservationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    /* 생성자 */
    public Reservation() {
    }

    public Reservation(Member member, Bus bus,
                       String startPoint, String endPoint,
                       LocalDateTime reservationDate,
                       ReservationState reservationState) {
        this.member = member;
        this.bus = bus;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.reservationDate = reservationDate;
        this.reservationState = reservationState;
    }

    /* 생성 메서드 */
    public static Reservation createReservation(Member member, Bus bus,
                                                String startPoint, String endPoint,
                                                LocalDateTime reservationDate) {
        Reservation reservation = new Reservation(member, bus, startPoint, endPoint,
                reservationDate, ReservationState.WAITING);

        return reservation;
    }

    /* 비지니스 로직 */
    public void changeReservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
    }

    public void cancel(){
        this.changeReservationState(ReservationState.CANCEL);
        bus.addSeats();
    }
}
