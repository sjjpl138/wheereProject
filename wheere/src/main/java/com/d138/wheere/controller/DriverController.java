package com.d138.wheere.controller;

import com.d138.wheere.controller.driverDTO.AssignBusDTO;
import com.d138.wheere.controller.driverDTO.DriverDTO;
import com.d138.wheere.controller.memberDTO.CheckBusDTO;
import com.d138.wheere.domain.*;
import com.d138.wheere.repository.bus.query.BusNumDirDTO;
import com.d138.wheere.service.BusService;
import com.d138.wheere.service.DriverService;
import com.d138.wheere.service.ReservationService;
import lombok.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final BusService busService;
    private final ReservationService reservationService;

//    @PostMapping
//    public ResponseEntity<String> signUpDriver(@ModelAttribute DriverDTO driverDTO) {
//
//        String driverId = driverDTO.getDId();
//        String dName = driverDTO.getDName();
//
//        Driver driver = new Driver();
//        driver.setId(driverId);
//        driver.setName(dName);
//        driver.setRatingScore(0);
//        driver.setRatingCnt(0);
//
//        Driver findDriver = driverService.findOne(driverId);
//        if (findDriver != null)
//            return  new ResponseEntity("이미 존재하는 회원입니다.", HttpStatus.BAD_REQUEST);
//
//        driverService.join(driver);
//        return new ResponseEntity(HttpStatus.OK);
//    }

    //로그인
    @PostMapping("/login")
    public  ResponseEntity logInDriver(String dId) {
        Driver findDriver = driverService.findOne(dId);

        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDId(findDriver.getId());
        driverDTO.setDName(findDriver.getName());

        List<BusNumDirDTO> busNumDirDTO = busService.inquireBusNumDir();

        LoginResponse response = new LoginResponse(driverDTO, busNumDirDTO);

        return new ResponseEntity(new ObjectResult(response), HttpStatus.OK);
    }

    //    버스 기사 평점 조회
    @GetMapping("/rate")
    public JSONObject searchRatingResult(@RequestParam("dId") String driverId) {

        double rateResult = driverService.checkRating(driverId);
        JSONObject response = new JSONObject();
        response.put("rate", rateResult );

        return response;
    }


    //버스 시간표 조회
    @GetMapping("/bus")
    public  ResponseEntity checkBusSchedule(CheckBusDTO checkBusTimeDTO) {
        List<LocalTime> busSchedule = busService.inquireBusDepartureTime(checkBusTimeDTO.getBNumber(), checkBusTimeDTO.getBDir());

        JSONArray scheduleResult = new JSONArray();
        scheduleResult.addAll(busSchedule);
        for (LocalTime time : busSchedule) {
            BusStartTimeDTO startTimeDTO = new BusStartTimeDTO();

            startTimeDTO.setBStartTime(time);
        }


        return new ResponseEntity(new BusSchedule(busSchedule), HttpStatus.OK);
    }

    //버스기사 버스 배정
    @PostMapping("/bus")
    public  ResponseEntity assignBus(@ModelAttribute AssignBusDTO assignBusDTO) {
        String driverId = assignBusDTO.getDId();
        String bNumber = assignBusDTO.getBNumber();
        BusState bDir = assignBusDTO.getBDir();
        LocalTime bStartTime = assignBusDTO.getBStartTime();
        LocalDate operationDate = assignBusDTO.getBOperationDate();

        driverService.selectBus(operationDate, bNumber, bDir, bStartTime, driverId);

        return new ResponseEntity(HttpStatus.OK);
    }

    //예약 승인
    @PostMapping("/resv/permit")
    public ResponseEntity permitResv(long rId) {
        reservationService.permitReservation(rId);
        return new ResponseEntity(HttpStatus.OK);
    }

    //예약 거부
    @PostMapping("/resv/reject")
    public ResponseEntity rejectResv(long rId) {
        reservationService.rejectReservation(rId);
        return new ResponseEntity(HttpStatus.OK);
    }

    //예약 완료
    @PostMapping("/resv/complete")
    public ResponseEntity completeResv (long rId) {
        reservationService.completeReservation(rId);
        return new ResponseEntity(HttpStatus.OK);
    }



    @Data
    @AllArgsConstructor
    static  class BusSchedule<T> {
        private T schedule;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class BusStartTimeDTO {
        private long bId;
        private LocalTime bStartTime;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static  class ResvResult {
        private long rId;
        private LocalDate rTime;
        private String rStart;
        private String rEnd;
        private String uId;
        private String uName;
        private LocalDate uBirthDate;
        private String uSex;
        private String uNumber;
    }

}
