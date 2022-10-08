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
}
