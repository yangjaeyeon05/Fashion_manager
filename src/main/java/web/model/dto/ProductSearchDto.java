package web.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ProductSearchDto {
    //08.12
    private int prodCatecode; //카테고리코드
    private int colorCode;    //색상코드
    private String prodSize;  //상품사이즈
    private String prodGender; //상품성별

    // 기간별 검색 시 사용되는 필드
    private String startDate;   // 기간 시작 날짜
    private String endDate;     // 기간 끝나는 날짜

    private String searchKeyword;   // 검색 조회 시 사용되는 필드의 값

    //금액별 검색시 사용 필드
    private int minPrice; //최소 금액
    private int maxPrice; //최대 금액
}
