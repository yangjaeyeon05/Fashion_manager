package web.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class OrderdetailDto { //class start
    //order Detail DTO
private int orddetailcode; //주문상세코드
private int ordcode;    //주문코드
private int proddetailcode; //상품상세코드
private int ordamount; //주문상품수량
private int ordstate; //주문상태
private int coupcode; //쿠폰코드
private int ordprice; //주문원가

/////////////////////////////////////////
    //order DTO
private String orddate; //주문날짜
///////////////////////////////////////////////
    //member Dto
    private String memname;
///////////////////////////////////////////////
    //product
    private String prodname;
///////////////////////////////////////////////
    //productdetail
    private String prodsize;
///////////////////////////////////////////////
    //coupon
    private String coupname;
} //class end
