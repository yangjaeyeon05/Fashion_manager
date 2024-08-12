package web.model.dto;

import lombok.*;

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
}
