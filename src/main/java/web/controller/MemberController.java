package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.sevice.MemberService;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    // 회원목록 출력
    @GetMapping("/list")
    public List<MemberDto> memberPrint(){
        return memberService.memberPrint();
    }

    // 회원 정보 수정(블랙리스트만)
    @PutMapping("/edit")
    public boolean memberEdit(int memcode, int blacklist){
        System.out.println(memcode);
        System.out.println(blacklist);
        return memberService.memberEdit(MemberDto.builder().memcode(memcode).blacklist(blacklist).build());
    }
}
