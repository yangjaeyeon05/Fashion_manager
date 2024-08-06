package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.service.SalesService;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/sales")
public class SalesController {
    @Autowired
    SalesService salesService;
    // 연매출 조회 (연도)
    @GetMapping("/yearly")
    public ArrayList<Map<String, String>> yearlySales (@RequestParam String year){
        return salesService.yearlySales(year);
    }
    // 월매출 조회 (연도, 월)
    @GetMapping("/monthly")
    public ArrayList<Map<String, String>> monthlySales (@RequestParam String year, @RequestParam String month){
        return salesService.monthlySales(year, month);
    }
    // 엑셀파일 입력

}