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

    // 4-1. 매출/정
    @GetMapping("/sales")
    public String sales(){return "/sales/sales.html";}

    // 4-2. 매출/상품분석
    @GetMapping("/sales/items")
    public String salesItem(){return "/sales/items.html";}

    // 4-3. 매출/매출추이분석
    @GetMapping("/sales/weekly")
    public String salesWeekly(){return "/sales/weekly.html";}
}   // class end
