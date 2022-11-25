package com.d138.wheere.controller;

import com.d138.wheere.controller.driverDTO.AssignBusDTO;
import com.d138.wheere.controller.driverDTO.DriverDTO;
import com.d138.wheere.controller.memberDTO.CheckBusDTO;
import com.d138.wheere.domain.*;
import com.d138.wheere.repository.bus.query.BusNumDirDTO;
import com.d138.wheere.service.BusService;
import com.d138.wheere.service.DriverService;
import com.d138.wheere.service.ReservationService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.json.simple.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
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
@CrossOrigin("*")
public class DriverController {

    private final DriverService driverService;
    private final BusService busService;
    private final ReservationService reservationService;

    //로그인
    @PostMapping("/login")
    public ResponseEntity logInDriver(String dId) {

        Driver findDriver = driverService.findOne(dId);
        DriverDTO driverDTO = new DriverDTO();
        driverDTO.setDId(findDriver.getId());
        driverDTO.setDName(findDriver.getName());

        List<BusNumDirDTO> busNumDirDTO = busService.inquireBusNumDir();
        DriverLoginDTO response = new DriverLoginDTO(driverDTO, busNumDirDTO);

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
        List<Bus> busSchedule = busService.inquireBusDepartureTime(checkBusTimeDTO.getBNumber(), checkBusTimeDTO.getBDir());

        List<BusStartTimeDTO> busStartTimeDTOS = new ArrayList<>();
        for (Bus bus : busSchedule) {
            BusStartTimeDTO busStartTime = new BusStartTimeDTO();

            busStartTime.setBId(bus.getId());
            busStartTime.setBStartTime(bus.getDepartureTime());
            busStartTimeDTOS.add(busStartTime);
        }

        return new ResponseEntity(new BusSchedule(busStartTimeDTOS), HttpStatus.OK);
    }

    //버스기사 버스 배정
    @PostMapping("/bus")
    public  ResponseEntity assignBus(@RequestBody AssignBusDTO assignBusDTO) {
        String driverId = assignBusDTO.getDId();
        Long bId = assignBusDTO.getBId();
        LocalDate bOperationDate = assignBusDTO.getBOperationDate();

        try {
//            driverService.selectBus();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    // 예약 조회
    @GetMapping("/resvs")
    public ResponseEntity resvResultByDriver (@RequestParam("bId") Long bId, @RequestParam("rTime") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate rDate) {

        List<Reservation> reservations = reservationService.checkScheduleByBus(bId, rDate);

        List<ResvResult> resvResult = new ArrayList<>();

        for (Reservation r : reservations) {
            Member m = r.getMember();
            ResvResult resvInfo = new ResvResult(r.getId(), r.getReservationDate(), r.getStartPoint(), r.getEndPoint(), m.getId(), m.getName(), m.getBirthDate(), m.getSex(), m.getPhoneNumber());
            resvResult.add(resvInfo);
        }

        return new ResponseEntity(new Reservations(resvResult), HttpStatus.OK);
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
    static class DriverLoginDTO<K, T> {
        private K driver;
        private T busList;
    }

    @Data
    @AllArgsConstructor
    static  class BusSchedule<T> {
        private T schedule;
    }

    @Data
    @AllArgsConstructor
    static class Reservations<T> {
        private T reservations;
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
        @JsonFormat(pattern ="yyyy-MM-dd")
        private LocalDate uBirthDate;
        private String uSex;
        private String uNumber;
    }
}