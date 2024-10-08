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
    @GetMapping("/todaytable")
    public ArrayList<SalesDto> weeklySales() {
        return salesService.weeklySales();
    }

    // [1] 연단위 매출 조회 (레코드 단위 : 연도)
    @GetMapping("/totaltable")
    public ArrayList<SalesDto> totalSales() {
        return salesService.totalSales();
    }

    // [1-1] 월단위 매출 조회 (레코드 단위 : 월)
    @GetMapping("/yearlytable")
    public ArrayList<SalesDto> yearlySales(@RequestParam int year) {
        return salesService.yearlySales(year);
    }

    // [1-2] 일단위 매출 조회 (레코드 : 일 단위)
    @GetMapping("/monthlytable")
    public ArrayList<SalesDto> monthlySales(@RequestParam int year, @RequestParam int month) {
        return salesService.monthlySales(year, month);
    }

    // [2] 연단위 판매된 제품 순위
    @GetMapping("/product/totaltable")
    public ArrayList<SalesDto> totalProducts() {
        return salesService.totalProducts();
    }

    // [2-1] 월단위 판매된 제품 순위
    @GetMapping("/product/yearlytable")
    public ArrayList<SalesDto> yearlyProducts(@RequestParam int year) {
        return salesService.yearlyProducts(year);
    }

    // [2-2] 일단위 판매된 제품 순위
    @GetMapping("/product/monthlytable")
    public ArrayList<SalesDto> monthlyProducts(@RequestParam int year, @RequestParam int month) {
        return salesService.monthlyProducts(year, month);
    }

    // [2-3] 색상 및 크기별 매출 현황, 날짜구간 2000-00-00 ~ 2000-00-00
    @GetMapping("/colorsizetable")
    public ArrayList<SalesDto> colorSize(@RequestParam String startDate, @RequestParam String endDate) {
        return salesService.colorSize(startDate, endDate);
    }

    // [3] 쿠폰코드별, 날짜구간 2000-00-00 ~ 2000-00-00
    @GetMapping("/coupontable")
    public ArrayList<SalesDto> coupons(@RequestParam String startDate, @RequestParam String endDate) {
        return salesService.coupons(startDate, endDate);
    }

    // [4] 판매추이 (최근 2주간)
    @GetMapping("/biweeklytable")
    public ArrayList<SalesDto> biweeklySales() {
        return salesService.biweeklySales();
    }

    // [5] 대비기간매출, 날짜구간1 2000-00-00 ~ 2000-00-00 vs 날짜구간2 2000-00-00 ~ 2000-00-00
    @GetMapping("/comparedatestable")
    public ArrayList<SalesDto> compareDates(@RequestParam String firstDateStart, @RequestParam String firstDateEnd, @RequestParam String secondDateStart, @RequestParam String secondDateEnd) {
        return salesService.compareDates(firstDateStart, firstDateEnd, secondDateStart, secondDateEnd);
    }

    // HTML : 연/월/일 테이블을 JS 에서 먼저 만들고 각 레코드를 대입해 날짜는 빠지지 않고 빈 날짜도 나올 수 있도록 한다
}
