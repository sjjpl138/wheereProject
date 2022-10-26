package com.d138.wheere.controller;

import com.d138.wheere.controller.memberDTO.*;
import com.d138.wheere.domain.Driver;
import com.d138.wheere.domain.Member;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.service.DriverService;
import com.d138.wheere.service.MemberService;
import com.d138.wheere.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ReservationService reservationService;
    private final DriverService driverService;

    //사용자 회원가입
    @PostMapping("/{uId}")
    public ResponseEntity signUpUser (@PathVariable("uId")String userId, @ModelAttribute MemberDTO memberDTO) {

        String name = memberDTO.getUName();
        LocalDate birthDate = memberDTO.getUBirthDate();
        String phoneNum = memberDTO.getUNum();
        String sex = memberDTO.getUSex();

        if (memberService.findMember(userId) != null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

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

        //버스

        return new ResponseEntity(memberDTO, HttpStatus.OK);
    }

    //예약하기
    @PostMapping("/resv")
    public ResponseEntity reserve(@ModelAttribute ReservationDTO resvDTO) {

        String uId = resvDTO.getUId();
        Long bId = resvDTO.getBId();
        String rStart = resvDTO.getRStart();
        String rEnd = resvDTO.getREnd();
        LocalDate rDate = resvDTO.getRDate();

        reservationService.saveReservation(uId, bId, rStart, rEnd, rDate);
        return new ResponseEntity(HttpStatus.OK);
    }

    //예약 결과 조회
    @GetMapping("/resvs")
    public List<ResvResultDTO> searchResv (@RequestParam("uId") String userId) {

        List<Reservation> reservationsByMember = reservationService.findReservationsByMember(userId);

        List<ResvResultDTO> resvResult = new ArrayList<>();

        for (int i = 0; i < reservationsByMember.size(); i++) {
            Reservation r =reservationsByMember.get(i);
            //isPaid 임의로 true,
            ResvResultDTO resvResultDTO = ResvResultDTO.createResResult(userId, true, r);
            resvResult.add(resvResultDTO);
        }
        return resvResult;
    }

    //예약 취소
    @PostMapping("/resv/{uId}")
    public ResponseEntity cancelResv(@PathVariable("uId") String userId,  Long rId) {
        Reservation resv = reservationService.findReservation(rId);

        CancelResultDTO cancelResult = new CancelResultDTO();
        cancelResult.setRid(rId);
        cancelResult.setUid(userId);
        // 취소 성공
        if (resv.canCancel()) {
            reservationService.cancelReservation(rId);
            return  new ResponseEntity(cancelResult, HttpStatus.OK);
        }
        //취소 실패
        return new ResponseEntity(cancelResult, HttpStatus.BAD_REQUEST);
    }

    //사용자 정보 수정
    @PutMapping("/{uId}")
    public  ResponseEntity updateUser (@PathVariable("uId") String userId, @ModelAttribute MemberDTO member) {

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUName(member.getUName());
        memberDTO.setUBirthDate(member.getUBirthDate());
        memberDTO.setUNum(member.getUNum());
        memberDTO.setUSex(member.getUSex());

        Member findMember = memberService.findMember(userId);
        String beforePhoneNumber = findMember.getPhoneNumber();

        memberService.modifyPhoneNumber(userId, member.getUNum());

        //전화번호 변경이 안됐을 경우
        if (beforePhoneNumber.equals(findMember.getPhoneNumber()))
            return new ResponseEntity(memberDTO, HttpStatus.BAD_REQUEST);
        return  new ResponseEntity(memberDTO, HttpStatus.OK);
    }

    //버스 기사 평점
    @PostMapping("/rate")
    public  ResponseEntity rateDriver (@ModelAttribute RateDriverDTO rateDriverDTO) {

        Driver findDriver = driverService.findDriver(rateDriverDTO.getDId());
        int beforeCnt = findDriver.getRatingCnt();

        driverService.reflectScores(findDriver.getBus().getId(), rateDriverDTO.getRate());

        if (findDriver.getRatingCnt() == beforeCnt)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity(HttpStatus.OK);
    }
}
