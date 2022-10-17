package com.d138.wheere.service;

import com.d138.wheere.domain.Member;
import com.d138.wheere.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원_등록_조회() {
        // Given
        Member member = new Member();
        member.setSex("F");
        member.setAge(22);
        member.setName("홍길동");
        member.setPhoneNumber("010-1111-1111");
        member.setId(1L);

        // When
        memberService.join(member);
        Member findMember = memberService.findMember(member.getId());

        // Then

        assertThat(findMember.getId()).isEqualTo(1L);
        assertThat(findMember.getAge()).isEqualTo(22);
        assertThat(findMember.getSex()).isEqualTo("F");
        assertThat(findMember.getName()).isEqualTo("홍길동");
        assertThat(findMember.getPhoneNumber()).isEqualTo("010-1111-1111");
        assertThat(findMember).isInstanceOf(Member.class);
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void 멤버_정보_수정() {
        // Given
        Member member = new Member();
        member.setSex("F");
        member.setAge(22);
        member.setName("홍길동");
        member.setPhoneNumber("010-1111-1111");
        member.setId(1L);

        // When
        memberService.join(member);
        memberService.modifyPhoneNumber(member.getId(), "010-1212-1212");
        Member findMember = memberService.findMember(member.getId());

        // Then
        System.out.println("findMember.getPhoneNumber() = " + findMember.getPhoneNumber());
        assertThat(findMember.getPhoneNumber()).isEqualTo("010-1212-1212");
    }
}
