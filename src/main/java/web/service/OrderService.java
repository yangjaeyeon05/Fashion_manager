package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.dao.OrderDao;
import web.model.dto.OrderdetailDto;
import web.model.dto.PagenationDto;

import java.util.ArrayList;

@Service
//0807 전체수정됨
public class OrderService {

    @Autowired
    OrderDao orderDao;
    
    //주문목록 날짜 출력함수
    public PagenationDto<OrderdetailDto> getorder(int category, int page, int size, String firstdate, String todayDate) {
//        System.out.println("page = " + page);
//        System.out.println("size = " + size);
        int offset = (page - 1) * size; //현재 페이지(1) - 1 * 표시될 데이터수(10)
//        System.out.println("offset = " + offset);

        ArrayList<OrderdetailDto> ordstat = orderDao.getorder(category, offset, size, firstdate, todayDate);

        int totalOrders = orderDao.getTotalOrdersCount(category, firstdate, todayDate);
        int totalPages = totalOrders / size + 1;

        ordstat.forEach(dto -> {
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //주문취소 목록 출력
    public PagenationDto<OrderdetailDto> ordCancel(int page, int size, String firstdate, String todayDate) {
//        System.out.println("page = " + page);
//        System.out.println("size = " + size);
        int offset = (page - 1) * size; //현재 페이지(1) - 1 * 표시될 데이터수(10)
//        System.out.println("offset = " + offset);

        ArrayList<OrderdetailDto> ordstat = orderDao.ordCancel(offset, size, firstdate, todayDate);

        int totalOrders = orderDao.getTotalOrdersCount2(firstdate, todayDate);
        int totalPages = totalOrders / size + 1;

        ordstat.forEach(dto -> {
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
///////////////////////////////////////////////////////////////////////////////////////
    // 주문상태 문자열로 변환 0807생성
    public void orderStr(OrderdetailDto orderdetailDto) {

        if (orderdetailDto.getOrdstate() == 1) {
            orderdetailDto.setOrdstateStr("주문완료");
        } else if (orderdetailDto.getOrdstate() == 2) {
            orderdetailDto.setOrdstateStr("배송시작");
        } else if (orderdetailDto.getOrdstate() == 3) {
            orderdetailDto.setOrdstateStr("반품");
        } else if (orderdetailDto.getOrdstate() == 4) {
            orderdetailDto.setOrdstateStr("취소");
        } else if (orderdetailDto.getOrdstate() == 5) {
            orderdetailDto.setOrdstateStr("정산완료");
        } else if (orderdetailDto.getOrdstate() == -4) {
            orderdetailDto.setOrdstateStr("취소완료");
        } else if (orderdetailDto.getOrdstate() == -3) {
            orderdetailDto.setOrdstateStr("반품완료");
        }
    }
//////////////////////////////////////////////////////////////////////
    // 주문 취소 확정 출력
    public boolean ordcheck(int orddetailcode) {
        return orderDao.ordcheck(orddetailcode);
    }
///////////////////////////////////////////////////////////////////////////
    //취소 완료 목록
    public PagenationDto<OrderdetailDto> cancelOrder(int page, int size ) {

        int offset = (page - 1) * size; //현재 페이지(1) - 1 * 표시될 데이터수(10)

        ArrayList<OrderdetailDto> ordstat = orderDao.cancelOrder(offset, size);

        int totalOrders = orderDao.getTotalOrdersCount3();
        int totalPages = totalOrders / size + 1;

        ordstat.forEach(dto -> {
            orderStr(dto);
        });
        return PagenationDto.<OrderdetailDto>builder()
                .page(page)
                .size(size)
                .totaldata(totalOrders)
                .totalPage(totalPages)
                .data(ordstat)
                .build();
    } //함수종료
    ///////////////////////////////////////////////////////////////////////////////////////////
    // 주문 반품 확정 처리
    public boolean returnCheck(int orddetailcode) {
        return orderDao.returnCheck(orddetailcode);
    }
/////////////////////////////////////////////////////////////////
    //반품완료 목록출력
    public PagenationDto<OrderdetailDto> returnOrd(int page, int size ) {

        int offset = (page - 1) * size; //현재 페이지(1) - 1 * 표시될 데이터수(10)

        ArrayList<OrderdetailDto> ordstat = orderDao.returnOrd(offset, size);

        int totalOrders = orderDao.getTotalOrdersCount4();
        int totalPages = totalOrders / size + 1;

        ordstat.forEach(dto -> {
            orderStr(dto);
        });
        return PagenationDto.<OrderdetailDto>builder()
                .page(page)
                .size(size)
                .totaldata(totalOrders)
                .totalPage(totalPages)
                .data(ordstat)
                .build();
    } //함수종료
    ///////////////////////////////////////////////////////////////////////////////////////////
    //반품목록 출력
    public PagenationDto<OrderdetailDto> ordReturn(int page, int size, String firstdate, String todayDate) {
//        System.out.println("page = " + page);
//        System.out.println("size = " + size);
        int offset = (page - 1) * size; //현재 페이지(1) - 1 * 표시될 데이터수(10)
//        System.out.println("offset = " + offset);

        ArrayList<OrderdetailDto> ordstat = orderDao.ordReturn(offset, size, firstdate, todayDate);

        int totalOrders = orderDao.getTotalOrdersCount5(firstdate, todayDate);
        int totalPages = totalOrders / size + 1;

        ordstat.forEach(dto -> {
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


} //class end