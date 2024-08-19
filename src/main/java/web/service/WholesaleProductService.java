package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import web.model.dao.WholesaleProductDao;
import web.model.dto.PologDto;
import web.model.dto.VendorDto;
import web.model.dto.WholesaleProductDto;

import java.util.List;

@Service
public class WholesaleProductService {
    @Autowired
    WholesaleProductDao wpdao;

    // 1. 거래처별 상품 목록
    public List<WholesaleProductDto> wpRead(int vendorcode){
        System.out.println("WholesaleProductService.wpRead");
        System.out.println("vendorcode = " + vendorcode);
        return wpdao.wpRead(vendorcode);
    }   // wpRead() end

    // 2. 발주
    public boolean doPo(PologDto pologDto){
        System.out.println("WholesaleProductService.doPo");
        System.out.println("pologDto = " + pologDto);
        return wpdao.doPo(pologDto);
    }   // doPo() end

    // 3. 발주현황 출력
    public List<PologDto> poRead(int quantitystate){
        System.out.println("WholesaleProductService.poRead");
        System.out.println("quantitystate = " + quantitystate);
        List<PologDto> list = wpdao.poRead(quantitystate);
        list.forEach(pologDto -> {
            String quantitystatename = convertQs(pologDto.getQuantitystate());
            pologDto.setQuantitystatename(quantitystatename);
        });
        return list;
    }   // poRead() end

    // * 처리상태 문자열로 바꾸기
    public String convertQs(int quantitystate){
        String quantitystatename = "";
        if(quantitystate == 1){
            quantitystatename = "진행중";
        }
        if(quantitystate == 2){
            quantitystatename = "도착완료";
        }
        return quantitystatename;
    }

    // 4. 발주 처리 상태 바꾸기
    public boolean poUpdate(int pocode){
        return wpdao.poUpdate(pocode);
    }   // poUpdate() end

    // 5. 재고로그추가
    public boolean invlogadd(int proddetailcode , int quantity){
        return wpdao.invlogadd(proddetailcode , quantity);
    }   // invlogadd() end

    // 6. 거래처불러오기
    public List<VendorDto> vencoderead(){
        System.out.println("WholesaleProductService.vencoderead");
        return wpdao.vencoderead();
    }   // vencoderead() end

    // 7. 도매상품 추가
    public boolean wpadd(WholesaleProductDto wpDto){
        System.out.println("WholesaleProductService.wpadd");
        System.out.println("wpDto = " + wpDto);
        return wpdao.wpadd(wpDto);
    }   // wpadd() end

    // 8. 도매상품 삭제
    public boolean wpdelete(int wpcode){
        System.out.println("WholesaleProductService.wpdelete");
        System.out.println("wpcode = " + wpcode);
        return wpdao.wpdelete(wpcode);
    }   // wpdelete() end


}   // class end
