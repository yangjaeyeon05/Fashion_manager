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
        return inventoryDao.inventoryRead();
    }

    //  재고 현황 업데이트1
    public boolean inventoryUpdate(InventoryDto inventoryDto){
        System.out.println("2번째 inventoryDto = " + inventoryDto);
        return inventoryDao.inventoryUpdate(inventoryDto);
    }

    // ===================================  2024-08-08 김민석 ========================================= //

    //  재고 현황 업데이트2


    //
    // ===================================  2024-08-12 김민석 ========================================= //


}
