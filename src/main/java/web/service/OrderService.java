package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.dao.OrderDao;
import web.model.dto.OrderDto;
import web.model.dto.OrderdetailDto;

import java.util.ArrayList;

@Service
//0807 전체수정됨
public class OrderService {

    @Autowired OrderDao orderDao;

    //주문목록 출력 함수
    public ArrayList<OrderdetailDto> getorder(){
//        System.out.println("OrderService.getorder");
        ArrayList<OrderdetailDto> ordstat =  orderDao.getorder();
        ordstat.forEach(dto -> {
            orderStr(dto);
        });
        return ordstat;
    }

    //주문목록 날짜 출력함수
    public ArrayList<OrderdetailDto> getorderdate(String firstdate,String todayDate){
//        System.out.println("OrderService.getorderdate");
        ArrayList<OrderdetailDto> ordstat = orderDao.getorderdate(firstdate,todayDate);
        ordstat.forEach( dto -> {
            orderStr(dto);
        });
        return ordstat;
    }

    //카테고리 목록 출력함수 0807 생성
    public ArrayList<OrderdetailDto> manage2 (int ordcatagory){
//        System.out.println("OrderController.manage2");
        ArrayList<OrderdetailDto> ordcate = orderDao.manage2(ordcatagory);
        ordcate.forEach( dto -> {
            orderStr( dto );
//            System.out.println("dto = " + dto); //확인용
        });
        return ordcate;
    }

    // 주문상태 문자열로 변환 0807생성
    public void orderStr( OrderdetailDto orderdetailDto ){

        if( orderdetailDto.getOrdstate() == 1 ){
            orderdetailDto.setOrdstateStr("주문완료");
        }else if( orderdetailDto.getOrdstate() == 2 ){
            orderdetailDto.setOrdstateStr("배송시작");
        }else if(orderdetailDto.getOrdstate() == 3){
            orderdetailDto.setOrdstateStr("반품");
        }else if(orderdetailDto.getOrdstate() == 4){
            orderdetailDto.setOrdstateStr("취소");
        }else if(orderdetailDto.getOrdstate() == 5){
            orderdetailDto.setOrdstateStr("정산완료");
        }
    }







}
