package com.d138.wheere.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @Column(name = "DRIVER_ID")
    private String id;

    private String name;

    private double ratingScore;

    private int ratingCnt;

    /* 비지니스 로직 */

    // 평점 계산
    public void calculateRatings(double score) {

        double sum = ratingCnt * ratingScore + score;
        this.ratingCnt++;
        this.ratingScore = sum / ratingCnt;
    }
}
