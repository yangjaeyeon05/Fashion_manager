package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // 1. 메인페이지
    @GetMapping("/")    // http://localhost:8080/ // 페이지 요청은 HTTP의 GET방식을 주로 사용한다.
    public String index(){
        return "/index.html";   // templates 폴더내 반환할 경로와 파일명
    }




    // 로그인페이지
    @GetMapping("/login")
    public String login(){
        return "/login.html";
    }

    // 회원 목록 페이지
    @GetMapping("/member/read")
    public String mRead(){
        return "/member.html";
    }
}   // class end
