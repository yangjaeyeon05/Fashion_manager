package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.MemberDto;
import web.model.dto.PagenationDto;
import web.service.MemberService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    MemberService memberService;

    // 회원목록 출력
    @GetMapping("/list")                            // Method 는 GET
    public PagenationDto<MemberDto> memberPrint(PagenationDto pagenationDto){           // 매개변수는 pagenationDto 를 받아서 memberService 에서 PagenationDto<MemberDto> 받아서 HTML 로 반환
        System.out.println("MemberController.memberPrint");
        System.out.println("pagenationDto = " + pagenationDto);
        return memberService.memberPrint(pagenationDto);
    }

    // 회원 정보 수정(블랙리스트만)
    @PutMapping("/edit")                                            // Method 는 PUT
    public boolean memberEdit(@RequestBody MemberDto memberDto){    // @RequestBody 로 js 에서 JSON 타입의 요청값을 받아서 memberDto 타입으로 받음
        return memberService.memberEdit(memberDto);                 // memberService 에서 memberEdit 에 memberDto 를 매개변수로 보내고 받은 boolean 값을 그대로 반환함.
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
    @GetMapping("/newrecommend")                                                // Method 는 GET
    public List<Map<String, String>> memberRecommend2(MemberDto memberDto){     // html, js 에서 memberDto 요청값을 받음
        System.out.println(memberDto);
        return memberService.memberRecommend2(memberDto);                       // memberService 에서 memberRecommend2 에 memberDto 를 매개변수로 보내고 받은 List 을 그대로 반환함.
    }

    // ===================================  2024-08-16 김민석 ========================================= //
}
