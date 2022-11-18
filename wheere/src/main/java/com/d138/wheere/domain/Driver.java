package com.d138.wheere.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Driver {

    @Id
    @Column(name = "DRIVER_ID")
    private String id;

    private String name;

    private double ratingScore;

    private int ratingCnt;

    /* 비지니스 로직 */

    // 버스 변경
    /*public void changeBus(Bus bus) {
        this.bus = bus;
    }*/

    // 평점 계산
    public void calculateRatings(double score) {

        double sum = ratingCnt * ratingScore;

        sum += score;

        ratingCnt++;

        ratingScore = sum / ratingCnt;
    }
}
