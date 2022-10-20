package com.d138.wheere.controller;

import com.d138.wheere.controller.driverDTO.DriverCancelResultDTO;
import com.d138.wheere.controller.driverDTO.DriverDTO;
import com.d138.wheere.controller.memberDTO.ResvResultDTO;
import com.d138.wheere.domain.Driver;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.service.DriverService;
import com.d138.wheere.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final ReservationService reservationService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUpDriver(DriverDTO driverDTO) {

        Driver driver = new Driver();
        driver.setId(driverDTO.getDid());
        driver.setName(driverDTO.getDname());
        driver.setRatingScore(0);
        driver.setRatingCnt(0);
        driver.setBus(null);

        driverService.join(driver);

        return new ResponseEntity(driverDTO, HttpStatus.OK);
    }

    //로그인
    @PostMapping("/login")
    public  ResponseEntity logInDriver(@RequestParam("did") String driverId) {
        Driver findDriver = driverService.findDriver(driverId);
        if (findDriver == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        DriverDTO driverDTO = new DriverDTO(findDriver.getId(), findDriver.getName(), findDriver.getBus().getBusNumber());
        return new ResponseEntity(driverDTO, HttpStatus.OK);
    }

    //예약 결과 조회
    @GetMapping("/resv/result")
    public List<ResvResultDTO> resvResultByDriver (@RequestParam("did") String driverId) {

        Driver findDriver = driverService.findDriver(driverId);
        List<Reservation> reservationsByDriver = reservationService.findReservationsByBus(findDriver.getBus().getId());

        List<ResvResultDTO> resvResult = new ArrayList<>();
        for (int i = 0; i < reservationsByDriver.size(); i++) {
            Reservation r =reservationsByDriver.get(i);
            //isPaid 임의로 true
            ResvResultDTO resvResultDTO = ResvResultDTO.createResResult(driverId, true,  r);
            resvResult.add(resvResultDTO);
        }
        return resvResult;
    }

    //버스 기사 평점
    @GetMapping("/rate/result")
    public double searchRatingResult(@RequestParam("did") String driverId) {
        Driver findDriver = driverService.findDriver(driverId);

        //double rateResult = driverService.(findDriver.getBId());

        return findDriver.getRatingScore();
    }

    @PostMapping("/resv/cancel")
    public DriverCancelResultDTO cancelResvByDriver(@RequestParam("did") Long driverId, @RequestParam("rid")Long resvId) {
        Reservation resv = reservationService.findReservation(resvId);

        if (resv.canCancel()) {
            reservationService.cancelReservation(resv.getId());
            return new DriverCancelResultDTO(resv.getBus().getId(),  resv.getId(),  true);
        }
        return new DriverCancelResultDTO(resv.getBus().getId(),  resv.getId(), false);
    }

}
