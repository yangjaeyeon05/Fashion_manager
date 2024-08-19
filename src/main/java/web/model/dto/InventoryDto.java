package web.model.dto;


import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    private int invlogcode;         // 재고로그코드
    private int proddetailcode;     // 상품코드
    private int invlogchange;       // 수량
    private int invlogdetail;       // 재고로그증감사유
    private String invdate;         // 재고로그날짜
    private int prodAmount;         // 재고수량합계
    // 상품이름
    private String prodname;
    // 컬러
    private String colorname;
    // 사이즈
    private String prodsize;
    // 처리상태 문자열 변경
    private String invlogdetailname;
}
