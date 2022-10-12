package com.d138.wheere.service;

import com.d138.wheere.repository.MemberRepository;
import com.d138.wheere.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final ReservationRepository reservationRepository;


}
