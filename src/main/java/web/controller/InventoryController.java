package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.InventoryDto;
import web.model.dto.ProductDto;
import web.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired private InventoryService inventoryService;

    //  재고 목록 출력
    @GetMapping("/read")
    public List<ProductDto> inventoryRead(){        // 매개변수는 없고 inventoryService 에서 LIST<ProductDto> 받아옴
        return inventoryService.inventoryRead();
    }

    //  재고 현황 업데이트1
    @PostMapping("/update")
    public boolean inventoryUpdate(@RequestBody InventoryDto inventoryDto){
        System.out.println("1번째 inventoryDto = " + inventoryDto);
        return inventoryService.inventoryUpdate(inventoryDto);
    }



    // ===================================  2024-08-08 김민석 ========================================= //

    @GetMapping("/update2")
    public boolean inventoryUpdate2(InventoryDto inventoryDto){
        System.out.println("inventoryDto = " + inventoryDto);
        return inventoryService.inventoryUpdate2(inventoryDto);
    }

    // ===================================  2024-08-12 김민석 ========================================= //
}
