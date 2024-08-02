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
}   // class end
