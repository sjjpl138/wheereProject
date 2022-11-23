package com.d138.wheere.controller;

import com.d138.wheere.controller.memberDTO.*;
import com.d138.wheere.domain.*;
import com.d138.wheere.repository.bus.query.BusNumDirDTO;
import com.d138.wheere.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final DriverService driverService;
    private final BusService busService;
    private final RouteService routeService;
    private final SeatService seatService;

    //사용자 회원가입
    @PostMapping
    public ResponseEntity signUpUser (@ModelAttribute MemberDTO memberDTO) {

        String userId = memberDTO.getUId();
        String name = memberDTO.getUName();
        LocalDate birthDate = memberDTO.getUBirthDate();
        String phoneNum = memberDTO.getUNum();
        String sex = memberDTO.getUSex();

        if (memberService.findMember(userId) != null)
            return new ResponseEntity("이미 존재하는 회원입니다.", HttpStatus.BAD_REQUEST);

        Member member = new Member(userId, name, birthDate, phoneNum, sex);
        memberService.join(member);

        return new ResponseEntity(HttpStatus.OK);
    }

    //사용자 로그인
    @PostMapping("/login")
    public  ResponseEntity logInUser (String uId) {

        Member findMember = memberService.findMember(uId);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUId(uId);
        memberDTO.setUName(findMember.getName());
        memberDTO.setUBirthDate(findMember.getBirthDate());
        memberDTO.setUNum(findMember.getPhoneNumber());
        memberDTO.setUSex(findMember.getSex());

        List<BusNumDirDTO> busNumDirDTO = busService.inquireBusNumDir();

        LoginResponse response = new LoginResponse(memberDTO, busNumDirDTO);

        return new ResponseEntity(new ObjectResult(response), HttpStatus.OK);
    }

    //사용자 정보 수정
    @PutMapping("/{uId}")
    public  ResponseEntity updateUser (@PathVariable("uId") String userId, @ModelAttribute MemberDTO memberDTO) {
        memberService.modifyPhoneNumber(userId, memberDTO.getUNum());

        return  new ResponseEntity(memberDTO, HttpStatus.OK);
    }

    //버스 기사 평점
    @PostMapping("/rate")
    public  ResponseEntity rateDriver (@ModelAttribute RateDriverDTO rateDriverDTO) {

        Reservation findResv = reservationService.findReservation(rateDriverDTO.getRId());
        Long bId = findResv.getBus().getId();
        LocalDate resvDate = findResv.getReservationDate();
        Double rate = rateDriverDTO.getRate();

        driverService.reflectScores(bId, resvDate, rate);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 8. 버스 시간표 조회
    @GetMapping("/bus")
    public  ResponseEntity checkBusSchedules(ScheduleDTO scheduleDTO) {

        String bNumber = scheduleDTO.getBNumber();
        BusState bDir = scheduleDTO.getBDir();
        LocalDate rDate = scheduleDTO.getRDate();
        int startSeq = scheduleDTO.getStartSeq();
        int endSeq = scheduleDTO.getEndSeq();

        List<BusDTO> busDTOList = new ArrayList<>();

        List<Long> busIdList = busService.inquireBusIdByBusNumAndDirection(bNumber, bDir);

        for (Long busId : busIdList) {
            LocalTime startTime = routeService.inquireTimeByBusAndSeq(busId, startSeq);
            LocalTime arrivalTime = routeService.inquireTimeByBusAndSeq(busId, endSeq);
            int leftSeatNum = seatService.inquiryMinLeftSeatNum(busId, rDate, startSeq, endSeq);

            BusDTO busDTO = new BusDTO(busId, startTime, arrivalTime, leftSeatNum);
            busDTOList.add(busDTO);
        }

        return new ResponseEntity(new BusSchedule(busDTOList), HttpStatus.OK);
    }

    // 버스 노선 조회
    @GetMapping("/bus/routes")
    public ResponseEntity checkBusRoute(CheckBusDTO checkBusDTO) {

        List<Route> routeList = routeService.findBusRouteByBusInfo(checkBusDTO.getBNumber(), checkBusDTO.getBDir());

        List<RouteDTO>routeDTOList = new ArrayList<>();


        for (Route route : routeList) {
            RouteDTO routeDTO = new RouteDTO();
            routeDTO.setStationSeq(route.getStationSeq());
            routeDTO.setStationName(route.getStation().getName());
            routeDTOList.add(routeDTO);
        }

        return new ResponseEntity(new BusRoute(routeDTOList), HttpStatus.OK);
    }



    // 예약 취소
    @PostMapping("/resv/cancel")
    public ResponseEntity cancelResv(Long rId) {
        reservationService.cancelReservation(rId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    static  class BusSchedule<T> {
        private T schedule;
    }

    @Data
    @AllArgsConstructor
    static class ScheduleDTO{
        private String bNumber;
        private BusState bDir;
        private LocalDate rDate;
        private int startSeq;
        private int endSeq;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class BusDTO {
        private Long bId;
        private LocalTime bStartTime;
        private LocalTime bArrivalTime;
        private int leftSeat;
    }

    @Data
    @AllArgsConstructor
    static  class BusRoute<T> {
        private T routes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class RouteDTO {
        private int stationSeq;
        private String stationName;
    }

    @Data
    @AllArgsConstructor
    static  class ResvResult {
        private String uId;
        private String bNumber;
        private long rId;
        private LocalDate rTime;
        private String rStart;
        private String rEnd;
        private ReservationState rState;
    }

    @Data
    @AllArgsConstructor
    static class ReservationList<T> {
        private T reservations;
    }
}