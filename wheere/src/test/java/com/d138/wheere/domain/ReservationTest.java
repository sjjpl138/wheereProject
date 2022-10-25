package com.d138.wheere.domain;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

public class ReservationTest {

    @Test
    public void enum테스트() {
        ReservationState reservationState = ReservationState.valueOf("CANCEL");;

        System.out.println(reservationState);
    }
}
