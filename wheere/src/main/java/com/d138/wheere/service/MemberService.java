package com.d138.wheere.service;

import com.d138.wheere.domain.Member;
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

    @Transactional
    public String join(Member member) {
        memberRepository.save(member);

        return member.getId();
    }

    @Transactional
    public void modifyPhoneNumber(String memberId, String phoneNumber) {
        Member findMember = memberRepository.findOne(memberId);
        findMember.changePhoneNumber(phoneNumber);
    }

    public Member findMember(String memberId) {
        return memberRepository.findOne(memberId);
    }
}
