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


    // 주문 목록 페이지
    @GetMapping("/order")
    public String order(){
        return "/order.html";
    }
    
    // 주문 반품 페이지
    @GetMapping("order/return")
    public String orderReturn(){
        return "/orderReturn.html";
    }

    // 주문 취소 페이지
    @GetMapping("/order/cancel")
    public String orderCancel(){
        return "/orderCancel";
    }
}   // class end
