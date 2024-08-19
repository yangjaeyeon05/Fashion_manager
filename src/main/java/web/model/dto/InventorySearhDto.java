package web.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class InventorySearhDto {
    private int invlogdetail;    // 증감사유 코드
    // 검색 필드
    private String searchKey;   // 검색 조회 시 사용되는 필드명
    private String searchKeyword;   // 검색 조회 시 사용되는 필드의 값
    // 기간별 검색 시 사용되는 필드
    private String startDate;   // 기간 시작 날짜
    private String endDate;     // 기간 끝나는 날짜
}
