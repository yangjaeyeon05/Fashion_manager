package web.model.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private int memcode;
    private String memname;
    private String memcontact;
    private String mememail;
    private String memgender;
    private int memcolor;
    private String memsize;
    private String memjoindate;
    private String memlastdate;
    private int blacklist;
    private String colorname;


}
