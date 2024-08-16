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

    // 상담목록 페이지 요청
    @GetMapping("/support")    // http://localhost:8080/ // 페이지 요청은 HTTP의 GET방식을 주로 사용한다.
    public String support(){
        return "/support/support.html";   // templates 폴더내 반환할 경로와 파일명
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

    // 주문 목록 페이지
    @GetMapping("/order")
    public String order(){
        return "/order/order.html";
    }

    // 주문 반품 페이지
    @GetMapping("order/return")
    public String orderReturn(){
        return "/order/orderReturn.html";
    }

    // 주문 취소 페이지
    @GetMapping("/order/cancel")
    public String orderCancel(){
        return "/order/orderCancel.html";
    }
    // 상품등록 페이지
    @GetMapping("/product/add")
    public String productAdd(){return "/product/productadd.html";}
    //08.08 상품목록 페이지
    @GetMapping("/product")
    public String productGetAll(){return "/product/productgetall.html";}

    // 4-1. 매출분석
    @GetMapping("/sales")
    public String sales(){return "/sales/sales.html";}

    // 4-1-1. 연매출분석
    @GetMapping("/sales/yearlyview")
    public String yearlySales(){return "/sales/yearly.html";}

    // 4-2. 매출/상품분석
    @GetMapping("/sales/items")
    public String salesItem(){return "/sales/items.html";}

    // 4-3. 매출/매출추이분석
    @GetMapping("/sales/weekly")
    public String salesWeekly(){return "/sales/weekly.html";}



    //  재고 관리 페이지 - 김민석
    @GetMapping("/product/inventory")   // http://localhost:8080/product/inventory
    public String inventory(){
        return "/product/inventory.html";
    }

    // 거래처관리페이지
    @GetMapping("/vendor")
    public String vendor(){return "/vendor/vendor.html";}
}   // class end
