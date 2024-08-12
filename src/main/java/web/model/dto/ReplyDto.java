package web.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ReplyDto {
    private int replycode;
    private int supcode;
    private String replycontent;
    private String replydate;
}
