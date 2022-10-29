package com.d138.wheere.controller;

import com.d138.wheere.controller.memberDTO.*;
import com.d138.wheere.domain.Driver;
import com.d138.wheere.domain.Member;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.exception.NotEnoughSeatsException;
import com.d138.wheere.repository.BusNumDirDTO;
import com.d138.wheere.service.BusService;
import com.d138.wheere.service.DriverService;
import com.d138.wheere.service.MemberService;
import com.d138.wheere.service.ReservationService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final DriverService driverService;
    private final BusService busService;

    //사용자 회원가입
    @PostMapping("/{uId}")
    public ResponseEntity signUpUser (@PathVariable("uId")String userId, @ModelAttribute MemberDTO memberDTO) {

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
    @PostMapping("/{uId}/login")
    public  ResponseEntity logInUser (@PathVariable("uId")String userId) {

        Member findMember = memberService.findMember(userId);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUName(findMember.getName());
        memberDTO.setUBirthDate(findMember.getBirthDate());
        memberDTO.setUNum(findMember.getPhoneNumber());
        memberDTO.setUSex(findMember.getSex());

        List<BusNumDirDTO> busNumDirDTO = busService.inquireBusNumDir();

        JSONObject jsonMember = new JSONObject();
        jsonMember.put("Member", memberDTO);

        JSONArray jsonBusNumDir = new JSONArray();
        jsonBusNumDir.addAll(busNumDirDTO);

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonMember);
        jsonArray.add(jsonBusNumDir);

        return new ResponseEntity(jsonArray, HttpStatus.OK);
    }

    //예약하기
    @PostMapping("/resv")
    public ResponseEntity reserve(@ModelAttribute ReservationDTO resvDTO) {

        String uId = resvDTO.getUId();
        Long bId = resvDTO.getBId();
        String rStart = resvDTO.getRStart();
        String rEnd = resvDTO.getREnd();
        LocalDate rDate = resvDTO.getRDate();

        try {
            Long rId = reservationService.saveReservation(uId, bId, rStart, rEnd, rDate);
            JSONObject rIdJsonResult = new JSONObject();
            rIdJsonResult.put("rId", rId);

            return new ResponseEntity(rIdJsonResult, HttpStatus.OK);
        } catch (IllegalStateException e)  {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotEnoughSeatsException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //예약 결과 조회
    @GetMapping("/resvs/{uId}")
    public List<ResvResultDTO> searchResv (@PathVariable("uId") String userId) {

        List<Reservation> reservationsByMember = reservationService.findReservationsByMember(userId);

        List<ResvResultDTO> resvResult = new ArrayList<>();

        for (Reservation reservation : reservationsByMember) {
            ResvResultDTO resvResultDTO = ResvResultDTO.createResResult(userId, true, reservation);
            resvResult.add(resvResultDTO);
        }

        JSONArray resultArray = new JSONArray();
        resultArray.addAll(resvResult);

        return resultArray;
    }

    //예약 거절
    @PostMapping("/resv/{uId}")
    public ResponseEntity cancelResv(@PathVariable("uId") String userId,  Long rId) {

        CancelResultDTO cancelResult = new CancelResultDTO();
        cancelResult.setRid(rId);
        cancelResult.setUid(userId);

        try {
            reservationService.cancelReservation(rId);
            return new ResponseEntity(cancelResult, HttpStatus.OK);
        } catch (IllegalStateException e)  {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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
        Driver findDriver = driverService.findDriverByBus(bId);

        int beforeCnt = findDriver.getRatingCnt();
        driverService.reflectScores(bId, rateDriverDTO.getRate());

        //변경 이전 별점과 반영 후 별점 비교
        if (findDriver.getRatingCnt() == beforeCnt)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity(HttpStatus.OK);
    }

    //버스 시간표 조회
    @GetMapping("/bus")
    public  ResponseEntity checkBusSchedules(CheckBusTimeDTO checkBusTimeDTO) {
        List<LocalTime> busSchedule = busService.inquireBusDepartureTime(checkBusTimeDTO.getBNumber(), checkBusTimeDTO.getBDir());

        JSONArray scheduleResult = new JSONArray();
        scheduleResult.addAll(busSchedule);

        return new ResponseEntity(scheduleResult, HttpStatus.OK);
    }
}