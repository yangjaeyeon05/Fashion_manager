package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import web.model.dto.OrderdetailDto;
import web.service.OrderService;

import java.util.ArrayList;

@RestController
@RequestMapping("/order")
public class OrderController { //class start

    @Autowired OrderService orderService;

    //주문목록 출력 함수
    @GetMapping("/getorder")
    public ArrayList<OrderdetailDto> getorder(@RequestParam("username")String args){
        System.out.println("OrderController.getorder");
        System.out.println("args = " + args);
        return orderService.getorder();
    }

}//class end
//