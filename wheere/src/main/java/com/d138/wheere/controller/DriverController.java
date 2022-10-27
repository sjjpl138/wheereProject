package com.d138.wheere.controller;

import com.d138.wheere.controller.driverDTO.AssignBusDTO;
import com.d138.wheere.controller.driverDTO.DriverCancelResultDTO;
import com.d138.wheere.controller.driverDTO.DriverDTO;
import com.d138.wheere.controller.memberDTO.CheckBusTimeDTO;
import com.d138.wheere.controller.memberDTO.ResvResultDTO;
import com.d138.wheere.domain.Bus;
import com.d138.wheere.domain.BusState;
import com.d138.wheere.domain.Driver;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.repository.BusNumDirDTO;
import com.d138.wheere.service.BusService;
import com.d138.wheere.service.DriverService;
import com.d138.wheere.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final ReservationService reservationService;
    private final BusService busService;

    @PostMapping("/{dId}")
    public ResponseEntity<String> signUpDriver(@PathVariable("dId") String driverId,  String dName) {

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

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDId(findDriver.getId());
        driverDTO.setDName(findDriver.getName());

        List<BusNumDirDTO> busNumDirDTO = busService.inquireBusNumDir();

        JSONObject jsonDriver = new JSONObject();
        jsonDriver.put("Driver", driverDTO);

        JSONArray jsonBusNumDir = new JSONArray();
        jsonBusNumDir.addAll(busNumDirDTO);

        JSONArray jsonResult = new JSONArray();
        jsonResult.add(jsonDriver);
        jsonResult.add(jsonBusNumDir);

        return new ResponseEntity(jsonResult, HttpStatus.OK);
    }

    //예약 결과 조회
    @GetMapping("/resvs/{bId}")
    public ResponseEntity resvResultByDriver (@PathVariable Long bId) {

        List<Reservation> reservationsByDriver = reservationService.findReservationsByBus(bId);

        List<ResvResultDTO> resvResult = new ArrayList<>();

        for (Reservation reservation : reservationsByDriver) {
            ResvResultDTO resvResultDTO = ResvResultDTO.createResResult(String.valueOf(bId), true, reservation);
            resvResult.add(resvResultDTO);
        }

        JSONArray jsonResult = new JSONArray();
        jsonResult.addAll(resvResult);

        return new ResponseEntity(jsonResult, HttpStatus.OK);
    }

//    버스 기사 평점 조회
    @GetMapping("/rate")
    public String searchRatingResult(@RequestParam String driverId) {

        double rateResult = driverService.checkRating(driverId);
        JSONObject response = new JSONObject();
        response.put("rRatingScore", rateResult );

        return response.toJSONString();
    }

    //버스 기사 예약 취소
    @PostMapping("/resv")
    public ResponseEntity cancelResvByDriver(@RequestParam("rId")Long rId) {
        Reservation resv = reservationService.findReservation(rId);

        DriverCancelResultDTO cancelResult = new DriverCancelResultDTO();
        cancelResult.setBId(resv.getBus().getId());
        cancelResult.setRId(rId);

        try {
            reservationService.cancelReservation(rId);

            return new ResponseEntity(cancelResult, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    //버스 시간표 조회
    @GetMapping("/bus")
    public  ResponseEntity checkBusSchedule(CheckBusTimeDTO checkBusTimeDTO) {
        List<LocalTime> busSchedule = busService.inquireBusDepartureTime(checkBusTimeDTO.getBNumber(), checkBusTimeDTO.getBDir());

        JSONArray scheduleResult = new JSONArray();
        scheduleResult.addAll(busSchedule);

        return new ResponseEntity(scheduleResult, HttpStatus.OK);
    }

    //버스기사 버스 배정
    @PostMapping("/bus/{dId}")
    public  ResponseEntity assignBus(@PathVariable("dId") String driverId, AssignBusDTO assignBusDTO) {
        String bNumber = assignBusDTO.getBNumber();
        BusState bDir = assignBusDTO.getBDir();
        LocalTime bStartTime = assignBusDTO.getBStartTime();
        Bus bus = busService.selectBus(bNumber, bDir, bStartTime);

        driverService.changeBus(driverId, bus);

        return new ResponseEntity(HttpStatus.OK);
    }

}
