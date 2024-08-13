package web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import web.model.dao.InventoryDao;
import web.model.dto.InventoryDto;
import web.model.dto.ProductDto;

import java.util.List;

@Service
public class InventoryService {


    @Autowired private InventoryDao inventoryDao;



    //  재고 목록 출력
    public List<ProductDto> inventoryRead(){        // 매개변수는 없고 inventoryDao 에서 LIST<ProductDto> 받아옴
        return inventoryDao.inventoryRead();        // inventoryDao 에서 받은 list 를 그대로 반환함.
    }

    //  재고 현황 업데이트1
    public boolean inventoryUpdate(InventoryDto inventoryDto){          // 매개변수로 inventoryDto 를 받고
        System.out.println("2번째 inventoryDto = " + inventoryDto);
        return inventoryDao.inventoryUpdate(inventoryDto);              // inventoryDao 의 inventoryUpdate 메소드에 inventoryDto 를 보내고 반환값을 그대로 반환함.
    }

    // ===================================  2024-08-08 김민석 ========================================= //

    //  재고 현황 업데이트2


    ////
    // ===================================  2024-08-12 김민석 ========================================= //

    //  재고 알림 메소드
    public String inventoryAlarm(InventoryDto inventoryDto){        // 매개변수로 inventoryDto 를 받고
        System.out.println("inventoryDto = " + inventoryDto);
        // inventoryDao 의 inventoryUpdate 메소드에 inventoryDto 를 보내서 받은 ProductDto 를  productDto 변수에 저장하고
        ProductDto productDto = inventoryDao.inventoryAlarm(inventoryDto);
        if(productDto.getProdAmount() <= 10) {                                  // 만약 productDto 에 저장되어 있는 prodAmount 값이 10 이하라면
            return "재고가 부족합니다.";                                           // String "재고가 부족합니다." 를 반환
        }
        return "";                                                              // 만약 productDto 에 저장되어 있는 prodAmount 값이 10 이하가 아니라면 String ""를 반환
    }

    // ===================================  2024-08-13 김민석 ========================================= //
}
