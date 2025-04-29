package org.com.jwtshop.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.com.jwtshop.domain.member.domain.Member;
import org.com.jwtshop.domain.member.dto.MemberSignUpRequest;
import org.com.jwtshop.domain.member.dto.MemberUpdateRequest;
import org.com.jwtshop.domain.member.exception.DuplicateMemberException;
import org.com.jwtshop.domain.member.exception.NotFoundUserException;
import org.com.jwtshop.domain.member.repository.MemberRepository;
import org.com.jwtshop.global.error.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    //생성자 주입 방식을 사용해야함 - 불변성 보장 등..
    private final MemberRepository memberRepository;
    //회원가입
    @Transactional
    public Long saveMember(MemberSignUpRequest memberSignUpRequest) {
        validateDuplicateMember(memberSignUpRequest);
        return memberRepository
                .save(memberSignUpRequest.toEntity())
                .getId();

    }
    //회원정보수정
    @Transactional
    public Long updateMember(Long id, MemberUpdateRequest memberUpdateRequest) {
        //수정 하는 회원 찾아
        Member member = findActiveMember(id);
        member.updateMember(memberUpdateRequest);

        return member.getId();
    }



    private void validateDuplicateMember(MemberSignUpRequest memberSignUpRequest) {
        //이름으로 중복을 테스트
        if (memberRepository.existsMemberByName(
                memberSignUpRequest.getName()
        )) {
            throw new DuplicateMemberException(ErrorCode.CONFLICT_ERROR);
        }
    }

    @Transactional(readOnly = true) //현재 삭제되지않은 사용자만 조회용
    public Member findActiveMember(Long id) {
        return  memberRepository
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.NOT_FOUND_RESOURCE));
    }

}
