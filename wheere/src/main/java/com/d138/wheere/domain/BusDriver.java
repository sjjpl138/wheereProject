package com.d138.wheere.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
// TODO (연관관계 설정하고 버스기사 평점 메기기 기능 추가)
public class BusDriver {

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

    public BusDriver(Bus bus, Driver driver, LocalDate operationDate) {
        this.bus = bus;
        this.driver = driver;
        this.operationDate = operationDate;
    }

    //== 생성 메서드 ==//
    public static BusDriver createBusDriver(Bus bus, Driver driver, LocalDate operationDate) {
        return new BusDriver(bus, driver, operationDate);
    }
}
