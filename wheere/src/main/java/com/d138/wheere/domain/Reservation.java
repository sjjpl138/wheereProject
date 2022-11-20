package com.d138.wheere.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
public class Reservation extends BaseEntity{

    // TODO (BaseEntity 추가하기)

    @Id
    @GeneratedValue
    @Column(name = "RESERVATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUS_ID")
    private Bus bus;

    private String startPoint;

    private String endPoint;

    private LocalDate reservationDate;

    @Enumerated(EnumType.STRING)
    private ReservationState reservationState;

    /* 생성자 */
    public Reservation() {
    }
    
    public Reservation(LocalDateTime createDate, LocalDateTime lastModifiedDate, Member member, Bus bus
            , String startPoint, String endPoint, LocalDate reservationDate, ReservationState reservationState) {
        super(createDate, lastModifiedDate);
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
                                                LocalDate reservationDate) {
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(now, now, member, bus, startPoint, endPoint,
                reservationDate, ReservationState.WAITING);

        return reservation;
    }

    /* 비지니스 로직 */
    public void changeReservationState(ReservationState reservationState) {
        this.reservationState = reservationState;
    }

    public void complete() {
        canComplete();

        LocalDateTime now = LocalDateTime.now();
        this.setLastModifiedDate(now);

        changeReservationState(ReservationState.COMP);
    }

    private void canComplete() {
        if(!this.reservationState.equals(ReservationState.RESERVED))
            throw new IllegalStateException("운행 완료할 수 없습니다.");
    }

    public void permit() {
        canPermitOrReject();

        LocalDateTime now = LocalDateTime.now();
        this.setLastModifiedDate(now);

        changeReservationState(ReservationState.RESERVED);
    }

    public void reject() {
        canPermitOrReject();

        LocalDateTime now = LocalDateTime.now();
        this.setLastModifiedDate(now);
        
        changeReservationState(ReservationState.REFUSED);
    }

    private void canPermitOrReject() {
        if(!this.reservationState.equals(ReservationState.WAITING))
            throw new IllegalStateException("예약 거절이 불가능한 상태입니다.");
    }

    public void cancel(){
        canCancel();

        LocalDateTime now = LocalDateTime.now();
        this.setLastModifiedDate(now);

        changeReservationState(ReservationState.CANCEL);
    }

    private void canCancel() {
        if (this.reservationState.equals(ReservationState.CANCEL) ||
                (this.reservationState.equals(ReservationState.COMP)) ||
                (this.reservationState.equals(ReservationState.REFUSED))) {
            throw new IllegalStateException("예약 취소가 불가능한 상태입니다.");
        }
    }
}
