package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.VendorDto;
import web.service.VendorService;

import java.util.List;

@RequestMapping("/vendor")
@RestController
public class VendorController {
    @Autowired
    VendorService vendorService;

    // 1. 거래처목록출력
    @GetMapping("/allread")
    public List<VendorDto> vendorallread(String searchKey , String searchKeyword){
        return vendorService.vendorallread(searchKey , searchKeyword);
    }   // vendorallread() end

    // 2. 거래처등록
    @PostMapping("/add")
    public boolean vendorAdd(@RequestBody VendorDto vendorDto){
        System.out.println("VendorController.vendorAdd");
        System.out.println();
        return vendorService.vendorAdd(vendorDto);
    }   // vendorAdd() end

    // 3. 거래처삭제
    @DeleteMapping("/delete")
    public boolean vendorDelete(int vendorcode){
        System.out.println("VendorController.vendorDelete");
        System.out.println("vendorcode = " + vendorcode);
        return vendorService.vendorDelete(vendorcode);
    }   // vendorDelete() end

    // 4. 수정 전 정보 가져오기
    @GetMapping("/read")
    public VendorDto infoRead(int vendorcode){
        System.out.println("VendorController.infoRead");
        System.out.println("vendorcode = " + vendorcode);
        return vendorService.infoRead(vendorcode);
    }   // infoRead() end

    // 5. 거래처정보수정
    @PutMapping("/update")
    public Boolean infoUpdate(@RequestBody VendorDto vendorDto){
        System.out.println("VendorController.infoUpdate");
        System.out.println("vendorDto = " + vendorDto);
        return vendorService.infoUpdate(vendorDto);
    }   // infoUpdate() end

}   // class end
