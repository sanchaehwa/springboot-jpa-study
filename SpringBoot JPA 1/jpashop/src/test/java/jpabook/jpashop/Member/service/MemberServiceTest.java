package jpabook.jpashop.Member.service;

import jpabook.jpashop.Member.domain.Address;
import jpabook.jpashop.Member.domain.Member;
import jpabook.jpashop.Member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

//테스트 요구사항 : 회원가입 성공, 회원가입 할떄 같은 이름이 있으면 예외 발생
@ExtendWith(SpringExtension.class) //Junit5 이기에 Junit4 RunWith 대체
@SpringBootTest
@Transactional

public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void join_member() throws Exception{
        //given
        String username = "test";

        Member member = Member.builder()
                .username(username)
                .build();
        //when
        Long saveId = memberService.join(member);
        //then
        assertThat(memberRepository.find(saveId)).isEqualTo(member);
     }

     @Test
    public void validateMemberSignUpTestRequest() throws Exception{
        //then
        //member1
         String username1 = "test1";

         Member member1 = Member.builder()
                 .username(username1)
                 .build();
         //member2
         String username2 = "test1";

         Member member2 = Member.builder()
                 .username(username2)
                 .build();
         //when
         memberService.join(member1);

         // then
         IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
             memberService.join(member2);
         });

         assertThat(exception.getMessage()).isEqualTo("이미 존재하는 회원입니다");
     }

}
