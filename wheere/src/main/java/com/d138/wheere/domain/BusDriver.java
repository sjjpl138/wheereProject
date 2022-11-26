package com.d138.wheere.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class BusDriver extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BUS_DRIVER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUS_ID")
    private Bus bus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DRIVER_ID")
    private Driver driver;
    
    // 버스 운행 날짜
    private LocalDate operationDate;

    // 버스 배정 상태
    private BusDriverStatus status;

    public BusDriver(LocalDateTime createDate, LocalDateTime lastModifiedDate, Bus bus, Driver driver, LocalDate operationDate) {
        super(createDate, lastModifiedDate);
        this.bus = bus;
        this.driver = driver;
        this.operationDate = operationDate;
        this.status = BusDriverStatus.ASSIGNED;
    }

    //== 생성 메서드 ==//
    public static BusDriver createBusDriver(Bus bus, Driver driver, LocalDate operationDate) {
        LocalDateTime now = LocalDateTime.now();
        return new BusDriver(now, now, bus, driver, operationDate);
    }

    public void complete() {

        changeLastModifiedDate();
        this.status = BusDriverStatus.COMP;
    }

    private void changeLastModifiedDate() {
        LocalDateTime now = LocalDateTime.now();
        this.setLastModifiedDate(now);
    }
}
