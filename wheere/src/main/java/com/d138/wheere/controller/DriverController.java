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

    @PostMapping("/{dId}")
    public ResponseEntity<String> signUpDriver(@PathVariable("dId")String driverId, @RequestParam("dName")String dName) {

        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setName(dName);
        driver.setRatingScore(0);
        driver.setRatingCnt(0);
        driver.setBus(null);

        Driver findDriver = driverService.findDriver(driverId);
        if (findDriver != null)
            return  new ResponseEntity(HttpStatus.BAD_REQUEST);
        driverService.join(driver);
        return new ResponseEntity(HttpStatus.OK);
    }

    //로그인
    @PostMapping("/{dId}/login")
    public  ResponseEntity logInDriver(@PathVariable("dId") String driverId) {
        Driver findDriver = driverService.findDriver(driverId);
        if (findDriver == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDId(findDriver.getId());
        driverDTO.setDName(findDriver.getName());
        driverDTO.setBNumber(findDriver.getBus().getBusNumber());

        return new ResponseEntity(driverDTO, HttpStatus.OK);
    }

    //예약 결과 조회
    @GetMapping("/resvs")
    public List<ResvResultDTO> resvResultByDriver (@PathVariable("dId") String driverId) {

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
    @GetMapping("/rate")
    public double searchRatingResult(@RequestParam("dId") String driverId) {
        Driver findDriver = driverService.findDriver(driverId);

        double rateResult = driverService.checkRating(driverId);

        return findDriver.getRatingScore();
    }

    @PostMapping("/resv/{dId}")
    public ResponseEntity cancelResvByDriver(@PathVariable("dId")Long driverId, @RequestParam("rId")Long resvId) {
        Reservation resv = reservationService.findReservation(resvId);

        DriverCancelResultDTO cancelResult = new DriverCancelResultDTO();
        cancelResult.setBId(resv.getBus().getId());
        cancelResult.setRId(resvId);

        if (resv.canCancel()) {
            reservationService.cancelReservation(resvId);
            return new ResponseEntity(cancelResult, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
