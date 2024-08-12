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

    int currentAmount = 0;              // 현재 재고 수량을 나타내는 지역 변수를 Service 에 선언하여 계산 처리함.

    //  재고 목록 출력
    public List<ProductDto> inventoryRead(){        // 매개변수는 없고 inventoryDao 에서 LIST<ProductDto> 받아옴
        List<ProductDto> list = inventoryDao.inventoryRead();   //  Dao 에서 받아온 List 를 다시 service 에서 지역 변수에 저장하고
        for (int i = 0; i < list.size(); i++) {                 //  반복문을 list 의 size()를 통해 받아온 list 의 길이만큼 돌려서
            list.get(i).setProdAmount(currentAmount);           //  list.get(i)를 통해 i번째 인덱스를 호출해서 setProdAmount 를 통해 현재 재고 수량을 나타내는 currentAmount 변수를 저장함
        }
        return list;
    }

    //  재고 현황 업데이트1
    public boolean inventoryUpdate(InventoryDto inventoryDto){
        System.out.println("2번째 inventoryDto = " + inventoryDto);
        return inventoryDao.inventoryUpdate(inventoryDto);
    }

    // ===================================  2024-08-08 김민석 ========================================= //

    //  재고 현황 업데이트2
    public boolean inventoryUpdate2(InventoryDto inventoryDto){
        List<ProductDto> list = inventoryDao.inventoryRead();
        InventoryDto inventoryDto1 = inventoryDao.inventoryUpdate2(inventoryDto);
        System.out.println("inventoryDto1.getProddetailcode() = " + inventoryDto1.getProddetailcode());
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getProdDetailcode() == inventoryDto1.getProddetailcode()) {
                System.out.println("list.get(i).getProdDetailcode() = " + list.get(i).getProdDetailcode());
                System.out.println(i + "번째 반복");
                if (inventoryDto1.getInvlogdetail() == 2) {
                    if (currentAmount <= 0) {
                        currentAmount = 0;
                    }
                    currentAmount -= inventoryDto1.getInvlogchange();
                } else {
                    currentAmount += inventoryDto1.getInvlogchange();
                }
                list.get(i).setProdAmount(currentAmount);
            }
        }
        return true;
    }

    //
    // ===================================  2024-08-12 김민석 ========================================= //


}
