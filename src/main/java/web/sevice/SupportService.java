package web.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.dao.SupportDao;
import web.model.dto.SupportDto;

import java.util.ArrayList;

@Service
public class SupportService {
    @Autowired
    SupportDao supportDao;

    // 1. 상담 목록 출력
    public ArrayList<SupportDto> supAllread(){
        System.out.println("SupportService.supAllread");
        ArrayList<SupportDto> supportDto = supportDao.supAllread();
        // supportDto
        return supportDto;
    }   // supportDto() end

    // 2. 카테고리 이름 변환
    public int tranceSupCa(){
        return supportDao.tranceSupCa();
    }   // tranceSupCa() end

}   // class end
