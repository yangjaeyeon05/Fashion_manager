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
    //08.16 상품수정 페이지
    @GetMapping("/product/edit")
    public String productEdit(){return "/product/productedit.html";}

    // [1] 연단위 매출 조회 페이지 (레코드 단위 : 연도)
    @GetMapping("/sales/total")
    public String totalsales(){return "/sales/total.html";}

    // [1-1] 월단위 매출 조회 페이지 (레코드 단위 : 월)
    @GetMapping("/sales/yearly")
    public String yearlySales(){return "/sales/yearly.html";}

    // [1-2] 일단위 매출 조회 페이지 (레코드 : 일 단위)
    @GetMapping("/sales/monthly")
    public String monthlySales(){return "/sales/monthly.html";}

    // [2] 연단위 판매된 제품 순위 페이지
    @GetMapping("/sales/product/total")
    public String totalItem(){return "/sales/product/total.html";}

    // [2-1] 월단위 판매된 제품 순위 페이지
    @GetMapping("/sales/product/yearly")
    public String yearlyItem(){return "/sales/product/yearly.html";}

    // [2-2] 일단위 판매된 제품 순위 페이지
    @GetMapping("/sales/product/monthly")
    public String monthlyItem(){return "/sales/product/monthly.html";}

    // [2-3] 색상 및 크기별 매출 현황 페이지
    @GetMapping("/sales/colorsize")
    public String colorSize(){return "/sales/colorsize.html";}

    // [3] 쿠폰코드별, 날짜구간 2000-00-00 ~ 2000-00-00
    @GetMapping("/sales/coupon")
    public String coupons(){return "/sales/coupon.html";}

    // [4] 판매추이 (최근 2주간)
    @GetMapping("/sales/biweekly")
    public String salesWeekly(){return "/sales/biweekly.html";}

    // [5] 대비기간매출 페이지, 날짜구간1 2000-00-00 ~ 2000-00-00 vs 날짜구간2 2000-00-00 ~ 2000-00-00
    @GetMapping("/sales/comparedates")
    public String compareDates(){return "/sales/comparedates.html";}

    //  재고 관리 페이지 - 김민석
    @GetMapping("/product/inventory")   // http://localhost:8080/product/inventory
    public String inventory(){
        return "/product/inventory.html";
    }

    // 거래처관리페이지
    @GetMapping("/vendor")
    public String vendor(){return "/vendor/vendor.html";}

    //  재고 관리 페이지
    @GetMapping("/product/inventorylog")
    public String inventorylog(){
        return "/product/inventorylog.html";
    }

}   // class end
