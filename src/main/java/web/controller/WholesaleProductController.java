package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.PologDto;
import web.model.dto.WholesaleProductDto;
import web.service.WholesaleProductService;

import java.util.List;

@RequestMapping("/wholesaleproduct")
@RestController
public class WholesaleProductController {
    @Autowired
    WholesaleProductService wpservice;

    // 1. 거래처별 상품 목록
    @GetMapping("/read")
    public List<WholesaleProductDto> wpRead(int vendorcode){
        System.out.println("WholesaleProductController.wpRead");
        System.out.println("vendorcode = " + vendorcode);
        return wpservice.wpRead(vendorcode);
    }   // wpRead() end

    // 2. 발주
    @PostMapping("/dopo")
    public boolean doPo(@RequestBody PologDto pologDto){
        System.out.println("WholesaleProductController.doPo");
        System.out.println("pologDto = " + pologDto);
        return wpservice.doPo(pologDto);
    }   // doPo() end

    // 3. 발주현황 출력
    @GetMapping("/poread")
    public List<PologDto> poRead(){
        System.out.println("WholesaleProductController.poRead");
        return wpservice.poRead();
    }   // poRead() end

    // 4. 발주 처리 상태 바꾸기
    @PutMapping("/update")
    public boolean poUpdate(int pocode){
        return wpservice.poUpdate(pocode);
    }   // poUpdate() end

    // 5. 재고로그추가
    @PostMapping("/invlogadd")
    public boolean invlogadd(int proddetailcode , int quantity){
        return wpservice.invlogadd(proddetailcode , quantity);
    }   // invlogadd() end
}   // class end
