package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.SalesDto;
import web.service.SalesService;

import java.util.ArrayList;

@RestController
@RequestMapping("/sales")
public class SalesController {
    @Autowired
    private SalesService salesService;

    // [0] (매출탭 기본페이지) 최근 일주일 매출 조회 (레코드 단위 : 일)
    @GetMapping("/today")
    public ArrayList<SalesDto> weeklySales(){
        return salesService.weeklySales();
    }

    // [1] 총 매출 조회 (레코드 단위 : 연도)
    @GetMapping("/total")
    public ArrayList<SalesDto> totalSales(){
        return salesService.totalSales();
    }

    // [1-1] 연매출 조회 (레코드 단위 : 월)
    @GetMapping("/yearly")
    public ArrayList<SalesDto> yearlySales(@RequestParam int year){
        return salesService.yearlySales(year);
    }

    // [1-2] 월간 매출 조회 (레코드 : 일 단위)
    @GetMapping("/monthly")
    public ArrayList<SalesDto> monthlySales(@RequestParam int year, @RequestParam int month){
        return salesService.monthlySales(year, month);
    }

    // [2] 총 판매된 제품 순위
    @GetMapping("/products/total")
    public ArrayList<SalesDto> totalProducts(){
        return salesService.totalProducts();
    }

    // [2-1] 연간 판매된 제품 순위
    @GetMapping("/products/yearly")
    public ArrayList<SalesDto> yearlyProducts(@RequestParam int year){
        return salesService.yearlyProducts(year);
    }

    // [2-2] 월간 판매된 제품 순위
    @GetMapping("/products/monthly")
    public ArrayList<SalesDto> monthlyProducts(@RequestParam int year, @RequestParam int month){
        return salesService.monthlyProducts(year, month);
    }

    // [3] 쿠폰코드별
    @GetMapping("/coupons")
    public ArrayList<SalesDto> coupons(){
        return salesService.coupons();
    }

    // [4] 판매추이 (최근 2주간)
    @GetMapping("/biweekly")
    public ArrayList<SalesDto> biweeklySales(){
        return salesService.biweeklySales();
    }

    // [5] 회원성향분석
    @GetMapping("/mempref")
    public ArrayList<SalesDto> memberPreferences(){
        return salesService.memberPreferences();
    }

    // [6] 대비기간매출, 날짜구간1 2000-00-00 ~ 2000-00-00 vs 날짜구간2 2000-00-00 ~ 2000-00-00
    @GetMapping("/comparedates")
    public ArrayList<ArrayList<SalesDto>> compareDates(@RequestParam String firstDate, @RequestParam String secondDate){
        return salesService.compareDates(firstDate, secondDate);
    }

    // [7] 색상 및 크기별 매출 현황, 날짜구간 2000-00-00 ~ 2000-00-00
    @GetMapping("/colorsize")
    public ArrayList<SalesDto> colorSize (@RequestParam String startDate, @RequestParam String endDate){
        return salesService.colorSize(startDate, endDate);
    }
    // TODO : 연/월/일 테이블 HTML로 미리 만들고 각 레코드를 대입
}
