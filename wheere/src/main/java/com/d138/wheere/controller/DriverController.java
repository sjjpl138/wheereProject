package com.d138.wheere.controller;

import com.d138.wheere.controller.driverDTO.DriverCancelResultDTO;
import com.d138.wheere.controller.driverDTO.DriverDTO;
import com.d138.wheere.controller.memberDTO.ResvResultDTO;
import com.d138.wheere.domain.Driver;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.service.DriverService;
import com.d138.wheere.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
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
    public ResponseEntity<String> signUpDriver(@PathVariable("dId")String driverId,  String dName) {

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
    public  ResponseEntity logInDriver(@PathVariable("dId") String driverId, String bNumber) {
        Driver findDriver = driverService.findDriver(driverId);
        //bNumber랑 did를 통해서 bid 받아오기
        //버스 변경
        //driverService.changeBus(driverId, busId);

        if (findDriver == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDId(findDriver.getId());
        driverDTO.setDName(findDriver.getName());
        driverDTO.setBNumber(bNumber);

        return new ResponseEntity(driverDTO, HttpStatus.OK);
    }

    //예약 결과 조회
    @GetMapping("/resvs")
    public List<ResvResultDTO> resvResultByDriver (@RequestParam String dId) {

        Driver findDriver = driverService.findDriver(dId);
        List<Reservation> reservationsByDriver = reservationService.findReservationsByBus(findDriver.getBus().getId());

        List<ResvResultDTO> resvResult = new ArrayList<>();

        for (int i = 0; i < reservationsByDriver.size(); i++) {
            Reservation r =reservationsByDriver.get(i);
            //isPaid 임의로 true
            ResvResultDTO resvResultDTO = ResvResultDTO.createResResult(dId, true,  r);
            resvResult.add(resvResultDTO);
        }

        return resvResult;
    }

    //버스 기사 평점
//    @GetMapping("/rate")
//    public String searchRatingResult(@RequestParam String dId) {
//
//        double rateResult = driverService.checkRating(dId);
//        JSONObject response = new JSONObject();
//        response.put("rRatingScore", rateResult );
//
//        return response.toJSONString();
//    }

    //버스 기사 예약 취소
    @PostMapping("/resv/{dId}")
    public ResponseEntity cancelResvByDriver(@PathVariable("dId")Long driverId, @RequestParam("rId")Long rId) {
        Reservation resv = reservationService.findReservation(rId);

        DriverCancelResultDTO cancelResult = new DriverCancelResultDTO();
        cancelResult.setBId(resv.getBus().getId());
        cancelResult.setRId(rId);

        if (resv.canCancel()) {
            reservationService.cancelReservation(rId);
            return new ResponseEntity(cancelResult, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
