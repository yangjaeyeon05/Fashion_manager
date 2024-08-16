package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import web.model.dao.VendorDao;
import web.model.dto.VendorDto;

import java.util.List;

@Service
public class VendorService {
    @Autowired
    VendorDao vendorDao;

    // 1. 거래처목록출력
    public List<VendorDto> vendorallread(String searchKey , String searchKeyword){
        return vendorDao.vendorallread(searchKey , searchKeyword);
    }   // vendorallread() end

    // 2. 거래처등록
    public boolean vendorAdd(VendorDto vendorDto){
        System.out.println("VendorService.vendorAdd");
        System.out.println("vendorDto = " + vendorDto);
        if(vendorDto.getVname()==""){
            return false;
        }
        return vendorDao.vendorAdd(vendorDto);
    }   // vendorAdd() end

    // 3. 거래처삭제
    public boolean vendorDelete(int vendorcode){
        System.out.println("VendorService.vendorDelete");
        System.out.println("vendorcode = " + vendorcode);
        return vendorDao.vendorDelete(vendorcode);
    }   // vendorDelete() end

    // 4. 수정 전 정보 가져오기
    public VendorDto infoRead(int vendorcode){
        System.out.println("VendorService.infoRead");
        System.out.println("vendorcode = " + vendorcode);
        return vendorDao.infoRead(vendorcode);
    }   // infoRead() end

    // 5. 거래처정보수정
    public Boolean infoUpdate(VendorDto vendorDto){
        System.out.println("VendorService.infoUpdate");
        System.out.println("vendorDto = " + vendorDto);
        return vendorDao.infoUpdate(vendorDto);
    }   // infoUpdate() end

}   // class end
