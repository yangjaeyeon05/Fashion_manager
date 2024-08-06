package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import web.model.dto.OrderdetailDto;
import web.service.OrderService;

import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/order")
public class OrderController { //class start

    @Autowired OrderService orderService;

    //주문목록 출력 함수
    @GetMapping("/getorder")
    public ArrayList<OrderdetailDto> getorder(){
//        System.out.println("OrderController.getorder");
        return orderService.getorder();
    }

    //주문목록 날짜 출력함수
    @GetMapping("/manage")
    public ArrayList<OrderdetailDto> getorderdate ( @RequestParam("firstdate") String firstdate,@RequestParam("todayDate") String todayDate){
        System.out.println("OrderController.getorderdate");
        System.out.println("firstdate = " + firstdate + ", todayDate = " + todayDate);
        return orderService.getorderdate(firstdate,todayDate);
    }

    //카테고리 목록 출력함수
    @GetMapping("/manage2")
    public ArrayList<OrderdetailDto> manage2 (){
        System.out.println("OrderController.manage2");
        return orderService.manage2();
    }

}//class end
//