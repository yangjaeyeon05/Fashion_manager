package web.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class SupportDto {
    private int supcode;
    private int memcode;
    private int supcategory;
    private String suptitle;
    private String supcontent;
    private String supdate;
    private int proddetailcode;
    private int supstate;
    private int ordcode;
    private String memname;
    // 미리 정한 카테고리 이름과 처리 상태 필드명
    private String supcategoryname;
    private String supstatename;
    // member 테이블과 연결 후 사용할 dto
    private String mememail;
    private String memcontact;
    // 상품코드에 따른 상품 이름
    private String prodname;
}   // class end
