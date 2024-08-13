package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.dao.OrderDao;
import web.model.dto.OrderDto;
import web.model.dto.OrderdetailDto;
import web.model.dto.PagenationDto;

import java.util.ArrayList;

@Service
//0807 전체수정됨
public class OrderService {

    @Autowired OrderDao orderDao;

    //주문목록 출력 함수 (페이지 기능 추가)
//    public PagenationDto<OrderdetailDto> getorder(int page, int size){
////        System.out.println("OrderService.getorder");
//        int offset = (page - 1) * size; //현재 페이지(1) - 1 * 표시될 데이터수(10)
//
//        ArrayList<OrderdetailDto> ordstat =  orderDao.getorder(offset, size); //주문상태 변환
//
//        int totalOrders = orderDao.getTotalOrdersCount(); //최종 주문 DAO의  카운터 된 횟수만큼 가져오기
//        int totalPages = totalOrders / size; //가지고온 횟수 / 표시될 데이터수(10개)
//
//        //주문상태 문자열로 변환
//        ordstat.forEach(dto -> {
//            orderStr(dto);
//        });
//
//        return PagenationDto.<OrderdetailDto>builder()
//                .page(page)
//                .size(size)
//                .totaldata(totalOrders)
//                .totalPage(totalPages)
//                .data(ordstat)
//                .build();
//    }

    //주문목록 날짜 출력함수
    public PagenationDto<OrderdetailDto> getorder(int category ,int page , int size, String firstdate,String todayDate){
        System.out.println("page = " + page);
        System.out.println("size = " + size);
        int offset = (page - 1) * size; //현재 페이지(1) - 1 * 표시될 데이터수(10)
        System.out.println("offset = " + offset);

        ArrayList<OrderdetailDto> ordstat = orderDao.getorder(category,offset,size,firstdate,todayDate);

        int totalOrders = orderDao.getTotalOrdersCount();
        int totalPages = totalOrders / size;

        ordstat.forEach( dto -> {
            orderStr(dto);
        });
        return PagenationDto.<OrderdetailDto>builder()
                .page(page)
                .size(size)
                .totaldata(totalOrders)
                .totalPage(totalPages)
                .data(ordstat)
                .build();
    }

//    //카테고리 목록 출력함수 0807 생성
//    public ArrayList<OrderdetailDto> manage2 (int ordcatagory){
////        System.out.println("OrderController.manage2");
//        ArrayList<OrderdetailDto> ordcate = orderDao.manage2(ordcatagory);
//        ordcate.forEach( dto -> {
//            orderStr( dto );
////            System.out.println("dto = " + dto); //확인용
//        });
//        return ordcate;
//    }

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
