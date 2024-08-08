package web.model.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    //08.08
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
    private MultipartFile uploadFile;
    private int prodAmount;
}
