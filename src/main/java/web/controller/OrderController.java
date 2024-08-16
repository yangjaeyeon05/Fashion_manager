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

@RestController
@RequestMapping("/order")
public class OrderController { //class start

    @Autowired OrderService orderService;

    //주문목록 날짜 출력함수
    @GetMapping("/manage")
    public PagenationDto<OrderdetailDto> getorder (int ordcatagory, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size ,@RequestParam("firstdate") String firstdate,@RequestParam("todayDate") String todayDate){
//        System.out.println("size = " + size);
//        System.out.println("page = " + page);
        return orderService.getorder(ordcatagory,page,size,firstdate,todayDate);
    }
    ///////////////////////////////////////////////////////////////////////////////
    @GetMapping("/ordCancel")
    public PagenationDto<OrderdetailDto> ordCancel (@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size ,@RequestParam("firstdate") String firstdate,@RequestParam("todayDate") String todayDate){
//        System.out.println("size = " + size);
//        System.out.println("page = " + page);
        return orderService.ordCancel(page,size,firstdate,todayDate);
    }
    ///////////////////////////////////////////////////////////////////////////////
    // 주문 취소 확정
    @PutMapping("/ordcheck")
    public boolean ordcheck (int orddetailcode){
        return orderService.ordcheck(orddetailcode);
    }
    ///////////////////////////////////////////////////////////////////////////////
    //취소확정 목록 호출 함수
    @GetMapping("/cancelord")
    public PagenationDto<OrderdetailDto> cancelOrder (@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size ){
//        System.out.println("size = " + size);
//        System.out.println("page = " + page);
        return orderService.cancelOrder(page,size);
    }
///////////////////////////////////////////////////////////////////////////////
    //반품목록 출력
@GetMapping("/ordReturn")
public PagenationDto<OrderdetailDto> ordReturn (@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size ,@RequestParam("firstdate") String firstdate,@RequestParam("todayDate") String todayDate){
//        System.out.println("size = " + size);
//        System.out.println("page = " + page);
    return orderService.ordReturn(page,size,firstdate,todayDate);
}
    ///////////////////////////////////////////////////////////////////////////////
    // 반품 확정
    @PutMapping("/returnCheck")
    public boolean returnCheck (int orddetailcode){
        return orderService.returnCheck(orddetailcode);
    }
    ///////////////////////////////////////////////////////////////////////////////
    //반품완료 목록 호출 함수
    @GetMapping("/returnOrd")
    public PagenationDto<OrderdetailDto> returnOrd (@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size ){
//        System.out.println("size = " + size);
//        System.out.println("page = " + page);
        return orderService.returnOrd(page,size);
    }
///////////////////////////////////////////////////////////////////////////////

}//class end
