package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.service.SalesService;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private SalesService salesService;

    // [1] 총 매출 조회 (레코드 단위 : 연도)
    @GetMapping("/total")
    public ArrayList<Map<String, String>> totalSales () {
        return salesService.totalSales();
    }
    // [2] 연매출 조회 (레코드 단위 : 월)
    @GetMapping("/yearly")
    public ArrayList<Map<String, String>> yearlySales (@RequestParam String year){
        return salesService.yearlySales(year);
    }
    // [3] 월간 매출 조회 (레코드 : 일 단위)
    @GetMapping("/monthly")
    public ArrayList<Map<String, String>> monthlySales (@RequestParam String year, @RequestParam String month){
        return salesService.monthlySales(year, month);
    }
    // [] 현재 테이블을 엑셀 파일로 내보내기
    @PostMapping("/excel")
    public boolean uploadExcel (@RequestBody MultipartFile excel){
        return (salesService.importExcel(excel));
    }
    // TODO : 연/월/일 테이블 HTML로 미리 만들고 각 레코드를 대입
}
