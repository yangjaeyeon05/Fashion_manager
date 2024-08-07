package web.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesDto {
    private int year;
    private int month;
    private int day;
    private int orders;
    private int returned;
    private int canceled;
    private int completed;
    private int revenue;
    private int saleAmount;
    private int income;
}
