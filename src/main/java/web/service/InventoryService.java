package web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import web.model.dao.InventoryDao;
import web.model.dto.*;

import java.util.List;

@Service
public class InventoryService {


    @Autowired private InventoryDao inventoryDao;



    //  재고 목록 출력
    public PagenationDto<ProductDto> inventoryRead(PagenationDto pagenationDto){        // 매개변수는 없고 inventoryDao 에서 LIST<ProductDto> 받아옴
        System.out.println("InventoryService.inventoryRead");
        System.out.println("pagenationDto = " + pagenationDto);

        // 이 페이지에서 내가 몇 번째부터 출력할 것인지 알려주는 변수
        // 계산식은 (현재 페이지 번호 - 1) X 내가 출력할 데이터의 갯수
        int offset = (pagenationDto.getPage() - 1) * pagenationDto.getSize();
        System.out.println("offset = " + offset);

        // 재고 수량 총 갯수를 inventoryDao 의 inventoryCount 메소드에서 계산해서 int 타입의  inventoryCount 변수에 저장함
        int inventoryCount = inventoryDao.inventoryCount();

        // 총 페이지 수 : 재고 총 갯수 % 내가 출력할 데이터의 갯수 = 0 이라면 재고 총 갯수 / 내가 출력할 데이터의 갯수 그대로 저장, 0이 아니라면 원래 계산식에서 +1함
        int totalpages = inventoryCount % pagenationDto.getSize() == 0 ? (inventoryCount / pagenationDto.getSize()) : (inventoryCount / pagenationDto.getSize()) + 1;
        System.out.println("totalpages = " + totalpages);

        // inventoryDao 의 inventoryRead 메소드에 pagenationDto 와 offset 를 보내서 list 를 반환 받고 저장함.
        List<ProductDto> list = inventoryDao.inventoryRead(pagenationDto, offset);

        return PagenationDto.<ProductDto>builder()      // PagenationDto 의 제네릭 타입을 ProductDto 로 지정해서 빌더로 생성
                .page(pagenationDto.getPage())          // page 에 pagenationDto.getPage()를 통해서 꺼내온 값 저장
                .size(pagenationDto.getSize())          // size 에 pagenationDto.getSize()를 통해서 꺼내온 값 저장
                .totaldata(inventoryCount)              // totaldata 에 재고 수량 총 갯수를 담은 변수인 inventoryCount 를 저장
                .totalPage(totalpages)                  // totalPage에 미리 계산한 총 페이지 수를 담은 변수인 totalpages 를 저장
                .data(list)                             // 조회된 게시물 정보 목록/리스트인 data 에 List<ProductDto> 타입의 list 변수를 저장
                .build(); // inventoryDao 의 inventoryRead 에서 받은 list 를 그대로 반환함.
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

    // 재고 현황 날짜별 그래프 출력
    public List<InventoryDto> inventoryChart(InventoryDto inventoryDto){        // 매개변수로 inventoryDto 를 받고
        System.out.println("inventoryDto = " + inventoryDto);
        return inventoryDao.inventoryChart(inventoryDto);                       // inventoryDao 에서 inventoryChart 에 inventoryDto 를 매개변수로 보내고 받은 List 을 그대로 반환함.
    }

    // ===================================  2024-08-14 김민석 ========================================= //
//    // 주문 완료 시 자동 업데이트
//    public boolean invAutoUpdateOrder(InventoryDto inventoryDto){
//        System.out.println("inventoryDto = " + inventoryDto);
//        int amount = inventoryDao.invAutoUpdateOrder(inventoryDto);
//        inventoryDto.setProdAmount(amount);
//        System.out.println("amount = " + amount);
//        return inventoryDao.invAutoUpdateOrder2(inventoryDto);
//    }

    // 주문 취소 시 자동 업데이트
    public boolean invAutoUpdateCancel(OrderdetailDto orderdetailDto){                  // 매개변수로 orderdetailDto 를 받고
        System.out.println("orderdetailDto = " + orderdetailDto);
        InventoryDto inventoryDto = inventoryDao.invAutoUpdateCancel(orderdetailDto);   // inventoryDao 에서 invAutoUpdateCancel 에 orderdetailDto 를 매개변수로 보내고 받은 inventoryDto 를 저장함.
        System.out.println("inventoryDto = " + inventoryDto);
        return inventoryDao.invAutoUpdateCancel2(inventoryDto);                         // inventoryDao 에서 invAutoUpdateCancel2 에 inventoryDto 를 매개변수로 보내고 받은 boolean 을 그대로 반환함.
    }

    // 반품 완료 시 자동 업데이트
    public boolean invAutoUpdateReturn(OrderdetailDto orderdetailDto){                  // 매개변수로 orderdetailDto 를 받고
        System.out.println("orderdetailDto = " + orderdetailDto);
        InventoryDto inventoryDto = inventoryDao.invAutoUpdateReturn(orderdetailDto);   // inventoryDao 에서 invAutoUpdateReturn 에 orderdetailDto 를 매개변수로 보내고 받은 inventoryDto 를 저장함.
        System.out.println("inventoryDto = " + inventoryDto);
        return inventoryDao.invAutoUpdateReturn2(inventoryDto);                         // inventoryDao 에서 invAutoUpdateReturn2 에 inventoryDto 를 매개변수로 보내고 받은 boolean 을 그대로 반환함.
    }

    // ===================================  2024-08-16 김민석 ========================================= //
}
