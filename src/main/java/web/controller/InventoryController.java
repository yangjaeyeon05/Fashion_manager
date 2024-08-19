package web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.InventoryDto;
import web.model.dto.InventorySearhDto;
import web.model.dto.OrderdetailDto;
import web.model.dto.PagenationDto;
import web.model.dto.ProductDto;
import web.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired private InventoryService inventoryService;

    //  재고 목록 출력
    @GetMapping("/read")                            // Method 는 GET
    public PagenationDto<ProductDto> inventoryRead(PagenationDto pagenationDto){        // 매개변수 pagenationDto 받고 inventoryService 에서 PagenationDto<ProductDto> 받아서 HTML 로 반환
        System.out.println("InventoryController.inventoryRead");
        System.out.println("pagenationDto = " + pagenationDto);
        return inventoryService.inventoryRead(pagenationDto);
    }

    //  재고 현황 업데이트1
    @PostMapping("/update")                                                     // Method 는 POST
    public boolean inventoryUpdate(@RequestBody InventoryDto inventoryDto){     // @RequestBody 로 js 에서 JSON 타입의 요청값을 받아서 inventoryDto 타입으로 받음
        System.out.println("1번째 inventoryDto = " + inventoryDto);
        return inventoryService.inventoryUpdate(inventoryDto);                  // inventoryService 에서 inventoryUpdate 에 inventoryDto 를 매개변수로 보내고 받은 boolean 값을 그대로 반환함.
    }



    // ===================================  2024-08-08 김민석 ========================================= //


    ////
    // ===================================  2024-08-12 김민석 ========================================= //
    // 재고 알림 메소드
    @GetMapping("/alarm")                                               // Method 는 GET
    public String inventoryAlarm(InventoryDto inventoryDto){            // html, js 에서 inventoryDto 요청값을 받음
        System.out.println("inventoryDto = " + inventoryDto);
        return inventoryService.inventoryAlarm(inventoryDto);           // inventoryService 에서 inventoryAlarm 에 inventoryDto 를 매개변수로 보내고 받은 String 값을 그대로 반환함.
    }
    // ===================================  2024-08-13 김민석 ========================================= //

    // 재고 현황 날짜별 그래프 출력
    @GetMapping("/chart")                                                   // Method 는 GET
    public List<InventoryDto> inventoryChart(InventoryDto inventoryDto){    // html, js 에서 inventoryDto 요청값을 받음
        System.out.println("inventoryDto = " + inventoryDto);
        return inventoryService.inventoryChart(inventoryDto);               // inventoryService 에서 inventoryChart 에 inventoryDto 를 매개변수로 보내고 받은 List 을 그대로 반환함.
    }

    // ===================================  2024-08-14 김민석 ========================================= //

//    // 주문 완료 시 자동 업데이트
//    @GetMapping("/autoorder")
//    public boolean invAutoUpdateOrder(InventoryDto inventoryDto){
//        System.out.println("inventoryDto = " + inventoryDto);
//        return inventoryService.invAutoUpdateOrder(inventoryDto);
//    }

    // 주문 취소 시 자동 업데이트
    @GetMapping("/autocancel")                                              // Method 는 GET
    public boolean invAutoUpdateCancel(OrderdetailDto orderdetailDto){      // html, js 에서 orderdetailDto 요청값을 받음
        System.out.println("orderdetailDto = " + orderdetailDto);
        return inventoryService.invAutoUpdateReturn(orderdetailDto);        // inventoryService 에서 invAutoUpdateReturn 에 inventoryDto 를 매개변수로 보내고 받은 boolean 을 그대로 반환함.
    }

    // 반품 완료 시 자동 업데이트
    @GetMapping("/autoreturn")                                              // Method 는 GET
    public boolean invAutoUpdateReturn(OrderdetailDto orderdetailDto){      // html, js 에서 orderdetailDto 요청값을 받음
        System.out.println("orderdetailDto = " + orderdetailDto);
        return inventoryService.invAutoUpdateCancel(orderdetailDto);        // inventoryService 에서 invAutoUpdateCancel 에 inventoryDto 를 매개변수로 보내고 받은 boolean 을 그대로 반환함.
    }

    // ===================================  2024-08-16 김민석 ========================================= //

    //  재고로그 출력 8/18 양재연
    @GetMapping("/invlogallread")
    public List<InventoryDto> invlogAllRead(InventorySearhDto inventorySearhDto){
        System.out.println("InventoryController.invlogAllRead");
       return inventoryService.invlogAllRead(inventorySearhDto);
    }   // invlogAllRead() end
}
