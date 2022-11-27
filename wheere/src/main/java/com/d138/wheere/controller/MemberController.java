package com.d138.wheere.controller;

import com.d138.wheere.controller.memberDTO.*;
import com.d138.wheere.domain.*;
import com.d138.wheere.exception.NotEnoughSeatsException;
import com.d138.wheere.repository.bus.query.BusNumDirDTO;
import com.d138.wheere.service.*;
import com.d138.wheere.service.SSE.NotificationService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private final NotificationService notificationService;
    private final BusDriverService busDriverService;

    //사용자 회원가입
    @PostMapping
    public ResponseEntity signUpUser(MemberDTO memberDTO) {

        String userId = memberDTO.getUId();
        String name = memberDTO.getUName();
        LocalDate birthDate = memberDTO.getUBirthDate();
        String phoneNum = memberDTO.getUNum();
        String sex = memberDTO.getUSex();

        System.out.println("memberDTO = " + memberDTO);

        System.out.println("===========================================");
        System.out.println("userId = " + userId);
        System.out.println("name = " + name);
        System.out.println("birthDate = " + birthDate);
        System.out.println("phoneNum = " + phoneNum);
        System.out.println("sex = " + sex);
        System.out.println("===========================================");

        Member member = new Member(userId, name, birthDate, phoneNum, sex);
        memberService.join(member);


        return new ResponseEntity(HttpStatus.OK);
    }

    //사용자 로그인
    @PostMapping("/login")
    public ResponseEntity logInUser(String uId) {

        System.out.println("=====================================");
        System.out.println("uId = " + uId);
        System.out.println("=====================================");

        Member findMember = memberService.findMember(uId);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUId(uId);
        memberDTO.setUName(findMember.getName());
        memberDTO.setUBirthDate(findMember.getBirthDate());
        memberDTO.setUNum(findMember.getPhoneNumber());
        memberDTO.setUSex(findMember.getSex());

        List<BusNumDirDTO> busNumDirDTO = busService.inquireBusNumDir();

        System.out.println("===================== test ===================");

        LoginResponse response = new LoginResponse(memberDTO, busNumDirDTO);

        System.out.println("===================== test2 ===================");


        return new ResponseEntity(new ObjectResult(response), HttpStatus.OK);
    }

    //사용자 정보 수정
    @PutMapping("/{uId}")
    public ResponseEntity updateUser(@PathVariable("uId") String userId, MemberDTO memberDTO) {
        System.out.println("memberDTO.getUNum() = " + memberDTO.getUNum());
        memberDTO.setUId(userId);
        memberService.modifyPhoneNumber(userId, memberDTO.getUNum());

        return new ResponseEntity(memberDTO, HttpStatus.OK);
    }

    //버스 기사 평점
    @PostMapping("/rate")
    public ResponseEntity rateDriver(RateDriverDTO rateDriverDTO) {

        Reservation findResv = reservationService.findReservation(rateDriverDTO.getRId());
        Long bId = findResv.getBus().getId();
        LocalDate resvDate = findResv.getReservationDate();
        Double rate = rateDriverDTO.getRate();

        System.out.println("===================================");
        System.out.println("bId = " + bId);
        System.out.println("rese = " + resvDate);
        System.out.println("rate = " + rate);
        System.out.println("===================================");

        driverService.reflectScores(bId, resvDate, rate);

        return new ResponseEntity(HttpStatus.OK);
    }

    // 8. 버스 시간표 조회
    @GetMapping("/bus")
    public ResponseEntity checkBusSchedules(ScheduleDTO scheduleDTO) {

        String bNumber = scheduleDTO.getBNumber();
        BusState bDir = scheduleDTO.getBDir();
        LocalDate rDate = scheduleDTO.getRDate();
        int startSeq = scheduleDTO.getStartSeq();
        int endSeq = scheduleDTO.getEndSeq();

        int leftSeatNum;

        List<BusDTO> busDTOList = new ArrayList<>();

        List<Long> busIdList = busService.inquireBusIdByBusNumAndDirection(bNumber, bDir);

        for (Long busId : busIdList) {
            LocalTime startTime = routeService.inquireTimeByBusAndSeq(busId, startSeq);
            LocalTime arrivalTime = routeService.inquireTimeByBusAndSeq(busId, endSeq);

            List<Integer> leftSeatNumList = seatService.inquireMinLeftSeatNum(busId, rDate, startSeq, endSeq);
            if (leftSeatNumList != null) {
                leftSeatNum = 2;
            } else {
                leftSeatNum = leftSeatNumList.get(0);
            }
            BusDTO busDTO = new BusDTO(busId, startTime, arrivalTime, leftSeatNum);
            busDTOList.add(busDTO);
        }

        return new ResponseEntity(new BusSchedule(busDTOList), HttpStatus.OK);
    }

    // 버스 노선 조회
    @GetMapping("/bus/routes")
    public ResponseEntity checkBusRoute(CheckBusDTO checkBusDTO) {

        System.out.println("=============================================");
        System.out.println("checkBusDTO.getBNumber() = " + checkBusDTO.getBNumber());
        System.out.println("checkBusDTO.getBDir() = " + checkBusDTO.getBDir());
        System.out.println("=============================================");

        List<Route> routeList = routeService.findBusRouteByBusInfo(checkBusDTO.getBNumber(), checkBusDTO.getBDir());

        List<RouteDTO> routeDTOList = new ArrayList<>();


        for (Route route : routeList) {
            RouteDTO routeDTO = new RouteDTO();
            routeDTO.setStationSeq(route.getStationSeq());
            routeDTO.setStationName(route.getStation().getName());
            routeDTOList.add(routeDTO);
        }

        return new ResponseEntity(new BusRoute(routeDTOList), HttpStatus.OK);
    }

    // 예약 조회
    @GetMapping("/resvs/{uid}")
    public ResponseEntity inquiryReservationByMember(@PathVariable("uid") String memberId) {

        System.out.println("memberId = " + memberId);

        List<Reservation> findReservations = reservationService.findReservationsByMember(memberId);

        List<ReservationDto> collect = findReservations.stream()
                .map(r -> {
                    Long busId = r.getBus().getId();
                    int startSeq = routeService.inquireSeqByBusAndStation(busId, r.getStartPoint());
                    int endSeq = routeService.inquireSeqByBusAndStation(busId, r.getEndPoint());
                    LocalTime bStartTime = routeService.inquireTimeByBusAndSeq(busId, startSeq);
                    LocalTime bArrivalTime = routeService.inquireTimeByBusAndSeq(busId, endSeq);
                    return new ReservationDto(memberId, r, bStartTime, bArrivalTime);
                })
                .collect(Collectors.toList());

        InquiryReservationResult inquiryReservationResult = new InquiryReservationResult(collect);

        return new ResponseEntity(new ReservationList(inquiryReservationResult), HttpStatus.OK);
    }

    // 예약하기
    @PostMapping("/resv")
    public ResponseEntity reserve(ReservationDTO resvDTO) {

        String uId = resvDTO.getUId();
        Long bId = resvDTO.getBId();
        int startSeq = resvDTO.getStartSeq();
        int endSeq = resvDTO.getEndSeq();
        LocalDate rTime = resvDTO.getRTime();

        System.out.println("=========================================");
        System.out.println("uId = " + uId);
        System.out.println("bId = " + bId);
        System.out.println("startSeq = " + startSeq);
        System.out.println("endSeq = " + endSeq);
        System.out.println("rTime = " + rTime);
        System.out.println("=========================================");

        try {
            Long rId = reservationService.saveReservation(uId, bId, startSeq, endSeq, rTime);
            Reservation findResv = reservationService.findReservation(rId);
            String startPoint = findResv.getStartPoint();
            String endPoint = findResv.getEndPoint();

            /**
             * SSE
             * send Data to client
             */
            String dId = new String();
            List<BusDriver> busDrivers = busDriverService.findDriverByBId(bId, rTime);
            System.out.println("busDrivers = " + busDrivers.size());
            if (busDrivers != null) {
                for (BusDriver bd : busDrivers) {
                    dId = bd.getDriver().getId();
                }
                notificationService.send(dId, rId, startPoint, endPoint, rTime);

                System.out.println("==================================");
                System.out.println("SSE_dId = " + dId);
                System.out.println("SSE_rId = " + rId);
                System.out.println("SSE_startPoint = " + startPoint);
                System.out.println("SSE_endPoint = " + endPoint);
                System.out.println("SSE_rTime = " + rTime);
                System.out.println("==================================");

            }

            return new ResponseEntity(HttpStatus.OK);

        } catch (IllegalStateException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotEnoughSeatsException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 예약 취소
    @PostMapping("/resv/cancel")
    public ResponseEntity cancelResv(Long rId) {
        reservationService.cancelReservation(rId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @Data
    static class ReservationDto {

        private String uId;
        private Long rId;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate rTime;
        private String rStart;
        private String rEnd;
        private boolean rIsPaid;
        private String bNumber;

        private BusState bDir;

        private ReservationState rState;

        @JsonFormat(pattern = "HH:mm:ss")
        private LocalTime bStartTime;
        @JsonFormat(pattern = "HH:mm:ss")
        private LocalTime bArrivalTime;

        public ReservationDto(String memberId, Reservation reservation, LocalTime bStartTime, LocalTime bArrivalTime) {
            uId = memberId;
            rId = reservation.getId();
            rTime = reservation.getReservationDate();
            rStart = reservation.getStartPoint();
            rEnd = reservation.getEndPoint();
            rIsPaid = true;
            bDir = reservation.getBus().getDirection();
            bNumber = reservation.getBus().getBusNumber();
            rState = reservation.getReservationState();
            this.bStartTime = bStartTime;
            this.bArrivalTime = bArrivalTime;
        }
    }

    @Data
    @AllArgsConstructor
    static class InquiryReservationResult<T> {
        private T reservations;
    }

    @Data
    @AllArgsConstructor
    static class BusSchedule<T> {
        private T schedule;
    }

    @Data
    @AllArgsConstructor
    static class ScheduleDTO {
        private String bNumber;
        private BusState bDir;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    static class BusRoute<T> {
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
    static class ResvResult {
        private String uId;
        private String bNumber;
        private long rId;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
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