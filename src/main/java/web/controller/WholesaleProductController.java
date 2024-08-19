package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.PologDto;
import web.model.dto.VendorDto;
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
    public List<PologDto> poRead(int quantitystate){
        System.out.println("WholesaleProductController.poRead");
        System.out.println("quantitystate = " + quantitystate);
        return wpservice.poRead(quantitystate);
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

    // 6. 거래처불러오기
    @GetMapping("/vencoderead")
    public List<VendorDto> vencoderead(){
        System.out.println("WholesaleProductController.vencoderead");
        return wpservice.vencoderead();
    }   // vencoderead() end

    // 7. 도매상품 추가
    @PostMapping("/wpadd")
    public boolean wpadd(@RequestBody WholesaleProductDto wpDto){
        System.out.println("WholesaleProductController.wpadd");
        System.out.println("wpDto = " + wpDto);
        return wpservice.wpadd(wpDto);
    }   // wpadd() end

    // 8. 도매상품 삭제
    @DeleteMapping("/wpdelete")
    public boolean wpdelete(int wpcode){
        System.out.println("WholesaleProductController.wpdelete");
        System.out.println("wpcode = " + wpcode);
        return wpservice.wpdelete(wpcode);
    }   // wpdelete() end
}   // class end
