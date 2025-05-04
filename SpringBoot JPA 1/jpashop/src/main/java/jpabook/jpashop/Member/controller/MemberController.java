package jpabook.jpashop.Member.controller;

import jakarta.validation.Valid;
import jpabook.jpashop.Member.domain.Address;
import jpabook.jpashop.Member.domain.Member;
import jpabook.jpashop.Member.dto.MemberForm;
import jpabook.jpashop.Member.service.MemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }
//회원가입
    @PostMapping()
    public String create(@Valid @ModelAttribute("memberForm") MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = Member.builder()
                .username(form.getName())
                .address(address)
                .build();
        memberService.join(member);
        return "redirect:/";
    }
    //회원 조회
    @GetMapping()
    public String list(Model model) {
        List<Member>members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}





