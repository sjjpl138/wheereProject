package com.d138.wheere.controller;

import com.d138.wheere.controller.memberDTO.CancelResultDTO;
import com.d138.wheere.controller.memberDTO.MemberDTO;
import com.d138.wheere.controller.memberDTO.ReservationDTO;
import com.d138.wheere.controller.memberDTO.ResvResultDTO;
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
    @PostMapping("/{uid}")
    public ResponseEntity signUpUser (@PathVariable("uid")String uid, @ModelAttribute MemberDTO memberDTO) {

//        Member member = new Member(uid, memberDTO.getUname(), memberDTO.getUage(), memberDTO.getUphonenumber(), memberDTO.getUsex());

//        memberService.join(member);

        return new ResponseEntity(HttpStatus.OK);
    }

    //사용자 로그인
    @PostMapping("/{uid}/login")
    public  ResponseEntity logInUser (@PathVariable("uid")String userId) {

        Member findMember = memberService.findMember(userId);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUname(findMember.getName());
//        memberDTO.setUage(findMember.getAge());
        memberDTO.setUphonenumber(findMember.getPhoneNumber());
        memberDTO.setUsex(findMember.getSex());

        return new ResponseEntity(memberDTO, HttpStatus.OK);
    }

    //예약하기
    @PostMapping("/resv")
    public ResponseEntity reserve(@ModelAttribute ReservationDTO reservationDTO) {

        System.out.println("손지민" + reservationDTO.getRdate());
        System.out.println(LocalDate.now());
        System.out.println(LocalDate.now().isBefore(reservationDTO.getRdate()));
        System.out.println(LocalDate.now().isEqual(reservationDTO.getRdate()));

        reservationService.saveReservation(reservationDTO.getUid(), reservationDTO.getBid(), reservationDTO.getStartpoint(), reservationDTO.getEndpoint(), reservationDTO.getRdate());
        return new ResponseEntity(HttpStatus.OK);
    }

    //예약 결과 조회
    @GetMapping("/resvs")
    public List<ResvResultDTO> searchResv (@RequestParam("uid") String userId) {

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
    @PostMapping("/resv/{uid}")
    public CancelResultDTO cancelResv(@PathVariable("uid") String userId, @RequestParam("rid") Long resvId) {
        Reservation resv = reservationService.findReservation(resvId);

        // 취소 성공
        if (resv.canCancel()) {
            reservationService.cancelReservation(resv.getId());
            return new CancelResultDTO(resv.getMember().getId(), resv.getId(), true);
        }
        //취소 실패
        return new CancelResultDTO(resv.getMember().getId(), resv.getId(), false);
    }

    //사용자 정보 수정
    @PutMapping("/{uid}")
    public  ResponseEntity updateUser (@PathVariable("uid") String userId, @ModelAttribute MemberDTO member) {

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUname(member.getUname());
        memberDTO.setUage(member.getUage());
        memberDTO.setUphonenumber(member.getUphonenumber());
        memberDTO.setUsex(member.getUsex());

        Member findMember = memberService.findMember(userId);
        String beforePhoneNumber = findMember.getPhoneNumber();

        memberService.modifyPhoneNumber(userId, member.getUphonenumber());

        //전화번호 변경이 안됐을 경우
        if (beforePhoneNumber.equals(findMember.getPhoneNumber()))
            return new ResponseEntity(memberDTO, HttpStatus.BAD_REQUEST);
        return  new ResponseEntity(memberDTO, HttpStatus.OK);
    }

    //버스 기사 평점
    @PostMapping("/rate")
    public  ResponseEntity rateDriver (@RequestParam("uid") String userId, @RequestParam("did") String driverId, @RequestParam("score") Long score) {

        Driver findDriver = driverService.findDriver(driverId);
        int beforeCnt = findDriver.getRatingCnt();

        driverService.reflectScores(findDriver.getBus().getId(), score);

        if (findDriver.getRatingCnt() == beforeCnt)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        return new ResponseEntity(HttpStatus.OK);
    }


}
