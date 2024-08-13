package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import web.model.dto.OrderdetailDto;
import web.model.dto.PagenationDto;
import web.service.OrderService;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController { //class start

    @Autowired OrderService orderService;

    //주문목록 출력 함수
//    @GetMapping("/getorder")
//    //@RequestParam 을 사용하여 page 의 기본값 1 페이지 출력수는 10으로 지정
//    public PagenationDto<OrderdetailDto> getorder(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
////        System.out.println("OrderController.getorder");
//        return orderService.getorder(page, size);
//    }

    //주문목록 날짜 출력함수
    @GetMapping("/manage")
    public PagenationDto<OrderdetailDto> getorder (int ordcatagory, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size ,@RequestParam("firstdate") String firstdate,@RequestParam("todayDate") String todayDate){
//        System.out.println("size = " + size);
//        System.out.println("page = " + page);
        return orderService.getorder(ordcatagory,page,size,firstdate,todayDate);
    }

//    //카테고리 목록 출력함수 0807 생성
//    @GetMapping("/manage2")
//    public ArrayList<OrderdetailDto> manage2 (int ordcatagory){
////        System.out.println("OrderController.manage2");
//        return orderService.manage2(ordcatagory);
//    }

}//class end
//