package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.dao.OrderDao;
import web.model.dto.OrderdetailDto;

import java.util.ArrayList;

@Service

public class OrderService {

    @Autowired OrderDao orderDao;

    //주문목록 출력 함수
    public ArrayList<OrderdetailDto> getorder(){
//        System.out.println("OrderService.getorder");
        return orderDao.getorder();
    }

    //주문목록 날짜 출력함수
    public ArrayList<OrderdetailDto> getorderdate(String firstdate,String todayDate){
        System.out.println("OrderService.getorderdate");
        return orderDao.getorderdate(firstdate,todayDate);
    }

    //카테고리 목록 출력함수
    public ArrayList<OrderdetailDto> manage2 (){
        System.out.println("OrderController.manage2");
        return orderDao.manage2();
    }

}
