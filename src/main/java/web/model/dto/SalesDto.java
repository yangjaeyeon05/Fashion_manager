package web.model.dto;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesDto {

    // 필드
    private int year; // 연도
    private int month; // 월
    private int day; // 일
    private int orders; // 총 주문건수
    private int ordered; // 총 주문량
    private int returned; // 반품 주문량
    private int canceled; // 취소된 주문량
    private int completed; // 완료된 주문량
    private int revenue; // 전체 수입
    private int saleAmount; // 할인된 금액
    private int income; // 실수익

    // 제품 필드
    private int prodcode; // 제품 코드
    private String prodname; // 제품 이름
    private int prodprice; // 제품 가격
    private String prodcatename; // 제품 분류명
    private String colorname; // 제품 색상명

    // 쿠폰 필드
    private String coupcode; // 쿠폰 고유코드
    private String coupname; // 쿠폰 이름
    private int coupsalerate; // 쿠폰 할인율
    private String coupexpdate; // 쿠폰 만기일

    // 색상 및 사이즈
    private Map<String, Integer> colorsize;

    // 2주간 판매추이
    private Map<String, Integer> biweeklysales;

    // 날짜구간비교
    private int firstcompleted; // 첫째 날짜 구간 실주문상품수
    private int firstincome; // 첫째 날짜 구간 실수익
    private int secondcompleted; // 둘째 날짜 구간 실주문상품수
    private int secondincome; // 둘째 날짜 구간 실수익
    private float completedcalc; // (주문1-주문2)/주문2 * 100 ( 퍼센티지 )
    private float incomecalc; // (수익1-수익2)/수익2 * 100 ( 퍼센티지 )

}
