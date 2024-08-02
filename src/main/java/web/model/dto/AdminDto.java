package web.model.dto;


import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminDto {
    private int admincode;
    private String adminid;
    private String adminpw;
}
