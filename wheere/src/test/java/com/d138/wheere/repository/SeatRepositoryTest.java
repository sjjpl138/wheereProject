package com.d138.wheere.repository;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Seat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class SeatRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    SeatRepository seatRepository;

    @Test
    public void findByBusAndDateTest() {

        // Given
        Bus bus = new Bus();
        bus.setId(1L);
        bus.setBusNumber("191");
        bus.setTotalWheelChairSeats(2);
        bus.setDirection(BusState.FORWARD);
        bus.setBusAllocationSeq(1);
        bus.setDepartureTime(LocalTime.now().plusMinutes(10));
        em.persist(bus);

        LocalDate reservationDate1 = LocalDate.now();

        Seat seat1 = new Seat(bus, reservationDate1, 2);

        // When
        seatRepository.save(seat1);

        List<Seat> findSeat1 = seatRepository.findByBusAndDate(bus.getId(), reservationDate1);

        // Then
        assertThat(findSeat1.get(0).getOperationDate()).isEqualTo(LocalDate.now());
        assertThat(findSeat1.get(0).getBus()).isEqualTo(bus);
    }
}
