package web.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class OrderDto {
    private int ordcode; //주문코드
    private int memcode;    //회원코드
    private String orddate; //주문날짜
}
