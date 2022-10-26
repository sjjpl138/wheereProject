package com.d138.wheere.service;

import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.repository.BusRepository;
import com.d138.wheere.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BusService {

    private final BusRepository busRepository;

    private final ReservationRepository reservationRepository;

    /*public List<Bus> findAllBus() {

    }*/
}
