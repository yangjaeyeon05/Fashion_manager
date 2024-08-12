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

    // [0] 최근 일주일 매출 조회 (레코드 단위 : 일)
    @GetMapping("/today")
    public ArrayList<SalesDto> weeklySales(){
        return salesService.weeklySales();
    }

    // [1] 총 매출 조회 (레코드 단위 : 연도)
    @GetMapping("/total")
    public ArrayList<SalesDto> totalSales () {
        return salesService.totalSales();
    }
    // [2] 연매출 조회 (레코드 단위 : 월)
    @GetMapping("/yearly")
    public ArrayList<SalesDto> yearlySales (@RequestParam int year){
        return salesService.yearlySales(year);
    }
    // [3] 월간 매출 조회 (레코드 : 일 단위)
    @GetMapping("/monthly")
    public ArrayList<SalesDto> monthlySales (@RequestParam int year, @RequestParam int month){
        return salesService.monthlySales(year, month);
    }
    // [] 현재 테이블을 엑셀 파일로 내보내기

    // TODO : 연/월/일 테이블 HTML로 미리 만들고 각 레코드를 대입
}
