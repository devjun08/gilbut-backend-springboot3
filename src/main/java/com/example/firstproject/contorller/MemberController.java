package com.example.firstproject.contorller;

import com.example.firstproject.dto.MemberForm;
import com.example.firstproject.entity.Member;
import com.example.firstproject.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
public class MemberController {
    @Autowired
    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/signup")
    public String newJoinForm() {
        return "members/new";
    }

    @PostMapping("/join")
    public String join(MemberForm form) {
        log.info(form.toString());
        // 1. dto -> entity
        Member member = form.toMember();
        log.info(member.toString());
        // 2. entity -> reposit
        Member saved = memberRepository.save(member);
        log.info(saved.toString());

        return "redirect:/members/" + saved.getId();
    }

    @GetMapping("/members/{id}")
    public String show(@PathVariable Long id, Model model) {
        log.info("id = " + id);
        Member memberEntity = memberRepository.findById(id).orElse(null);
        model.addAttribute("member", memberEntity);
        return "members/show";
    }

    @GetMapping("/members")
    public String index(Model model) {
        List<Member> memberList = memberRepository.findAll();
        model.addAttribute("memberList", memberList);
        return "members/index";
    }

    @GetMapping("/members/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        // id로 디비에서 데이터 찾기
        Member memberEntity = memberRepository.findById(id).orElse(null);
        // 모델에 등록
        model.addAttribute("members", memberEntity);
        return "members/edit";
    }

    @PostMapping("/members/update")
    public String update(MemberForm form) {
        log.info(form.toString());
        // 1. form to entity
        Member memberEntity = form.toMember();
        log.info(memberEntity.toString());
        // 2. save entity to db
        // 2 - 1 DB의 기본 데이터값 가져오기
        Member Target = memberRepository.findById(memberEntity.getId()).orElse(null);
        // 2 - 2 갱신하기
        if (Target != null) {
            memberRepository.save(memberEntity);
        }
        // 3. redirect
        return "redirect:/members/" + memberEntity.getId();
    }
}
