package web.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class PologDto {
    private int pocode;             // 발주코드
    private int wpcode;             // 도매상품코드
    private int quantity;           // 주문수량
    private int totalamount;        // 총주문금액
    private String quantitydate;    // 주문날짜
    private String arrivaldate;     // 도착날짜
    private int quantitystate;      // 처리상태
    private String wpname;          // 상품이름
    private String prodsize;        // 사이즈
    private String colorname;       // 컬러
    private String vname;           // 거래처명
    private String quantitystatename;   // 처리상태문자
    private int proddetailcode;         // 상품코드
}
