//package com.d138.wheere.controller;
//
//import com.d138.wheere.controller.driverDTO.DriverCancelResultDTO;
//import com.d138.wheere.controller.driverDTO.DriverDTO;
//import com.d138.wheere.controller.driverDTO.DriverResvResultDTO;
//import com.d138.wheere.domain.Driver;
//import com.d138.wheere.domain.Reservation;
//import com.d138.wheere.service.DriverService;
//import com.d138.wheere.service.ReservationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/driver")
//@RequiredArgsConstructor
//public class DriverController {
//
//    private final DriverService driverService;
//    private final ReservationService reservationService;
//
//    @PostMapping("/signup")
//    public ResponseEntity<String> signUpDriver(DriverDTO driverDTO) {
//
//        Driver driver = new Driver();
//        driver.setId(driverDTO.getDid());
//        driver.setName(driverDTO.getDname());
//        driver.setRatingScore(0);
//        driver.setRatingCnt(0);
//
//        driverService.saveDriver(driver);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    //로그인
//    @PostMapping("/login")
//    @ResponseBody
//    public  DriverDTO logInDriver(@RequestParam("did") Long driverId) {
//        Driver findDriver = driverService.findOne(driverId);
//
//        DriverDTO driverDTO = new DriverDTO(findDriver.getId(), findDriver.getName(), findDriver.getBus().getBusNumber());
//
//        return driverDTO;
//    }
//
//    //예약 결과 조회
//    @GetMapping("/resv/result")
//    @ResponseBody
//    public DriverResvResultDTO reserve (@RequestParam("did") Long driverId) {
//
//        Driver findDriver = driverService.findOne(driverId);
//        List<Reservation> reservationsByBus = reservationService.findReservationsByBus(findDriver.getBus().getId());
//
//    }
//
//    //버스 기사 평점
//    @GetMapping("/rate/result")
//    public ?? searchRatingResult(@RequestParam("did") Long driverId) {
//        Driver findDriver = driverService.findOne(driverId);
//
//
//    }
//
//    @PostMapping("/resv/cancel")
//    @ResponseBody
//    public DriverCancelResultDTO cancelResvByDriver(@RequestParam("did") Long driverId, @RequestParam("rid")Long resvId) {
//        Reservation resv = reservationService.findReservation(resvId);
//
//        if (resv.canCancel()) {
//            reservationService.cancelReservation(resv.getId());
//            return new DriverCancelResultDTO(resv.getBus().getId(),  resv.getId(), true);
//        }
//        return new DriverCancelResultDTO(resv.getBus().getId(),  resv.getId(), false);
//    }
//
//}
