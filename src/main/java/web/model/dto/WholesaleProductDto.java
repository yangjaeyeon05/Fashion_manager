package web.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class WholesaleProductDto {
    private int wpcode;
    private String wpname;
    private int wpcost;
    private int proddetailcode;
    private int vendorcode;
    // 판매할때 상품이름
    private String prodname;
    // 거래처이름
    private String vname;
    // 사이즈
    private String prodsize;
    // 컬러코드
    private String colorname;
    // 재고
    private int inv;
}
