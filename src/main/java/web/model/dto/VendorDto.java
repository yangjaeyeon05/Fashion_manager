package web.model.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class VendorDto {
    private int vendorcode;
    private String vname;
    private String vcontact;
    private String vaddress;
    private String vdate;
}
