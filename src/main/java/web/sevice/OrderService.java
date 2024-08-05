package web.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.dao.OrderDao;
import web.model.dto.OrderdetailDto;

import java.util.ArrayList;

@Service

public class OrderService {

    @Autowired OrderDao orderDao;

    //주문목록 출력 함수
    public ArrayList<OrderdetailDto> getorder(){
        System.out.println("OrderService.getorder");
        return orderDao.getorder();
    }

}
