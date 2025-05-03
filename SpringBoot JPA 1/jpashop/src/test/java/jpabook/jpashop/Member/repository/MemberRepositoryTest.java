package jpabook.jpashop.Member.repository;

import jpabook.jpashop.Member.domain.Member;
import jpabook.jpashop.Member.dto.MemberSignUpTestRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class) //Junit5 이기에 Junit4 RunWith 대체
@SpringBootTest
class MemberRepositoryTest {
    private static MemberSignUpTestRequest memberSignUpRequest;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    public void testMember() throws Exception{
        //given
        String username = "test";
        memberSignUpRequest = new MemberSignUpTestRequest(username);

        Member member = Member.builder()
                                .username(memberSignUpRequest.getName())
                                .build();

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);
        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        System.out.println("(findMember == member) = " + (findMember == member));

    }
}

