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
    public boolean inventoryUpdate(@RequestBody InventoryDto inventoryDto){     // @RequestBody 로 js 에서 JSON 타입의 요청값을 받아서 inventoryDto 타입으로 받음
        System.out.println("1번째 inventoryDto = " + inventoryDto);
        return inventoryService.inventoryUpdate(inventoryDto);                  // inventoryService 에서 inventoryUpdate 에 inventoryDto 를 매개변수로 보내고 받은 반환값을 그대로 반환함.
    }



    // ===================================  2024-08-08 김민석 ========================================= //


    ////
    // ===================================  2024-08-12 김민석 ========================================= //
    // 재고 알림 메소드
    @GetMapping("/alarm")
    public String inventoryAlarm(InventoryDto inventoryDto){            // html, js 에서 inventoryDto 요청값을 받음
        System.out.println("inventoryDto = " + inventoryDto);
        return inventoryService.inventoryAlarm(inventoryDto);           // inventoryService 에서 inventoryAlarm 에 inventoryDto 를 매개변수로 보내고 받은 String 값을 그대로 반환함.
    }
    // ===================================  2024-08-13 김민석 ========================================= //

    @GetMapping("/chart")
    public List<InventoryDto> inventoryChart(InventoryDto inventoryDto){
        System.out.println("inventoryDto = " + inventoryDto);
        return inventoryService.inventoryChart(inventoryDto);
    }

    // ===================================  2024-08-14 김민석 ========================================= //
}
