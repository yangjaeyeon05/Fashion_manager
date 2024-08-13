package web.model.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class PagenationDto<T> {
    // - 검색,카테고리별 페이징처리에 다양한 매개변수가 필요하므로 집합 Dto
    private int page; // 1. 현재 페이지 번호

    // (지역/매개) 변수 와 필드 차이점 : 초기값의 차이
    private int size; // 2. 한 페이지에 표시될 데이터 수 (10개씩)
    private int totalPage; // 3. 전체 페이지수
    private List<T> data; // 4. 조회된 게시물 정보 목록/리스트 //제네릭
    private int startBtn; // 5. 페이지별 시작버튼 번호
    private int endBtn; // 6. 페이지별 끝버튼 번호
    private int totaldata; //7. 전체 데이터수
    private int bcno; // 8. 현재 카테고리 번호

    // + 검색 필드
    private String searchKey;   // 9. 검색 조회시 사용되는 필드명
    private String searchKeyword; // 10. 검색 조회시 사용되는 필드의 값

}
