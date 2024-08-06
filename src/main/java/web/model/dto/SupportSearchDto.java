package web.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class SupportSearchDto {
    private int supcode;    // 문의유형 코드
    private int supstate;   // 처리상태 코드
    // 검색 필드
    private String searchKey;   // 검색 조회 시 사용되는 필드명
    private String searchKeyword;   // 검색 조회 시 사용되는 필드의 값
}
