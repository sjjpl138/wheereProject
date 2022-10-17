package com.d138.wheere.service;

import com.d138.wheere.domain.Driver;
import com.d138.wheere.domain.Member;
import com.d138.wheere.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    @Transactional
    public String join(Driver driver) {
        driverRepository.save(driver);

        return driver.getId();
    }

//    @Transactional
//    public void modifyPhoneNumber(Long memberId, String phoneNumber) {
//        Member findMember = memberRepository.findOne(memberId);
//        findMember.changePhoneNumber(phoneNumber);
//    }
//
//    public Member findMember(Long memberId) {
//        return memberRepository.findOne(memberId);
//    }
}
