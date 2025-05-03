package jpabook.jpashop.Member.service;

import jpabook.jpashop.Member.domain.Member;
import jpabook.jpashop.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor //Lombok 생성자 주입 (final인 필드만 가지고 생성자를 만듬)

//JPA의 모든 변경을 데이터베이스에 반영(커밋) 하기 위해 트랜잭션을 열고 관리
@Transactional(readOnly = true) //조회만 하는 경우이니깐 성능의 최적화를 위해 읽기전용으로 설정
public class MemberService {
    //@Autowired : 필드주입 / 생성자 주입
    private final MemberRepository memberRepository;

    //회원가입
    @Transactional //회원가입은 쓰기작업이 필요하니 별도의 에노테이션 추가
    public Long join(Member member) {

        validateDuplicateMember(member); //중복회원
        memberRepository.save(member);

//        memberRepository.save(member);
//        return member.getId();
        return member.getId();
    }

    //중복회원이면 예외 발생
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getUsername());
        //Exception
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }


    //회원 조회(전체)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    //회원 조회(개별)
    public Member findOne(Long id) {
        return memberRepository.find(id);
    }
}
