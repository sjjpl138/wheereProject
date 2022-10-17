package com.d138.wheere.controller;

import com.d138.wheere.controller.memberDTO.CancelResultDTO;
import com.d138.wheere.controller.memberDTO.MemberDTO;
import com.d138.wheere.controller.memberDTO.ResvResultDTO;
import com.d138.wheere.domain.Member;
import com.d138.wheere.domain.Reservation;
import com.d138.wheere.service.MemberService;
import com.d138.wheere.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final ReservationService reservationService;

    //사용자 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser (@ModelAttribute MemberDTO memberDTO) {

        Member member = new Member(memberDTO.getUid(), memberDTO.getUname(), memberDTO.getUage(), memberDTO.getUphonenumber(), memberDTO.getUsex());

        //멤버 확인
        if (memberService.findMember(member.getId()) != null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        memberService.join(member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //사용자 로그인
    @PostMapping("/login")
    public MemberDTO logInUser (@RequestParam("uid") String userId) {
        Member findMember = memberService.findMember(userId);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUid(findMember.getId());
        memberDTO.setUname(findMember.getName());
        memberDTO.setUage(findMember.getAge());
        memberDTO.setUphonenumber(findMember.getPhoneNumber());
        memberDTO.setUsex(findMember.getSex());

        return memberDTO;
    }

    //예약하기
//    @PostMapping("/resv")
//    public ResvResultDTO reserve(@ModelAttribute ResvResultDTO resvResultDTO) {
//    }

    //예약 결과 조회
    @GetMapping("/resv/result")
    public List<ResvResultDTO> searchResv (@RequestParam("uid") String userId) {

        List<Reservation> reservationsByMember = reservationService.findReservationsByMember(userId);

        List<ResvResultDTO> resvResult = new ArrayList<>();

        for (int i = 0; i < reservationsByMember.size(); i++) {
            Reservation r =reservationsByMember.get(i);
            //isPaid 임의로 true
            ResvResultDTO resvResultDTO = ResvResultDTO.createResResult(userId, true, r);
            resvResult.add(resvResultDTO);
        }
        return resvResult;
    }

    //예약 취소
    @PostMapping("/resv/cancel")
    public CancelResultDTO cancelResv(@RequestParam("uid") Long userId, @RequestParam("rid") Long resvId) {
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
    @PostMapping("/update")
    public MemberDTO updateUser (@ModelAttribute MemberDTO member) {

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUid(member.getUid());
        memberDTO.setUname(member.getUname());
        memberDTO.setUage(member.getUage());
        memberDTO.setUphonenumber(member.getUphonenumber());
        memberDTO.setUsex(member.getUsex());

        memberService.modifyPhoneNumber(member.getUid(), member.getUphonenumber());

        //전화번호 변경이 안됐을 경우
        if (memberService.findMember(member.getUid()).getPhoneNumber() ==  member.getUphonenumber()) {
            //return null; ??
        }
        return  memberDTO;
    }

    //버스 기사 평점
    @PostMapping("/rate")
    public  ResponseEntity<String> rateDriver (@RequestParam("uid") Long userId, @RequestParam("did") Long driverId) {

        // Driver driver = driverService.findDriver(driverId);

        // 버스 기사 평점 반영
        // 평점 개수 추가(+1)

        //if (driverService.findDriver(driver.getId()).driver.getRatingCnt() == driver.getRatingCnt())
        // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
