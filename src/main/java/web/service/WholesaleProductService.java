package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import web.model.dao.WholesaleProductDao;
import web.model.dto.PologDto;
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
    public List<PologDto> poRead(){
        System.out.println("WholesaleProductService.poRead");
        List<PologDto> list = wpdao.poRead();
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


}   // class end
