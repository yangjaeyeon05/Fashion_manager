package web.model.dto;


import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {

    private int invlogcode;
    private int proddetailcode;
    private int invlogchange;
    private int invlogdetail;
    private String invdate;
    private int prodAmount;
}
