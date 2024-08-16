package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.service.MemberService;

import java.util.List;
import java.util.Map;

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
    public boolean memberEdit(@RequestBody MemberDto memberDto){

        return memberService.memberEdit(memberDto);
    }

    // -------------------------- 2024-08-05 ---------------------------------------- //


//    // 회원 사이즈에 맞는 옷 추천
//    @GetMapping("/recommend")
//    public List<Map<String, String>> memberRecommend(MemberDto memberDto){
//        System.out.println("memberDto = " + memberDto);
//        memberService.memberRecommend(memberDto);
//        return memberService.SizeRecommend(memberDto);
//    }

    // -------------------------- 2024-08-07 ---------------------------------------- //

    // 그 날 많이 팔린 물품 + 나의 성별 + 선호 사이즈에 따른 제품 추천
    @GetMapping("/newrecommend")
    public List<Map<String, String>> memberRecommend2(MemberDto memberDto){
        System.out.println(memberDto);
        return memberService.memberRecommend2(memberDto);
    }

    // ===================================  2024-08-16 김민석 ========================================= //
}
