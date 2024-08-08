package web.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private int prodCode;
    private String prodName;
    private int prodPrice;
    private String prodGender;
    private String prodDesc;
    private int prodDetailcode;
    private String prodSize;
    private String prodFilename;
    private String prodDate;
    private int colorCode;
    private String colorName;
    private int prodCatecode;
    private String prodCatename;
    private int prodAmount;
}
