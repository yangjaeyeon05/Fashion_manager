package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.dao.SupportDao;
import web.model.dto.SupportDto;
import web.model.dto.SupportSearchDto;

import java.util.ArrayList;

@Service
public class SupportService {
    @Autowired
    SupportDao supportDao;

    // 1. 상담 목록 출력
    public ArrayList<SupportDto> supAllread(SupportSearchDto supportSearchDto){
        System.out.println("SupportService.supAllread");
        System.out.println("supportSearchDto = " + supportSearchDto);
        ArrayList<SupportDto> supportDto = supportDao.supAllread(supportSearchDto);
        // supportDto
        for(SupportDto dto : supportDto){
            // 숫자로 나오는 코드 미리 정한 이름으로 변환해서 dto에 저장하기
            // db에서 받은 숫자 코드 매개변수로 넘겨주기
            String supcategoryname = convertSupCa(dto.getSupcode());
            String supstatename = convertSupState(dto.getSupstate());
            dto.setSupcategoryname(supcategoryname);
            dto.setSupstatename(supstatename);
        }
        System.out.println("supportDto = " + supportDto);
        return supportDto;
    }   // supportDto() end

    // 2. 카테고리 이름 변환
    public String convertSupCa(int supcode){
        String supcategoryname = "";
        if(supcode==1){
            supcategoryname = "반품문의";
        }
        if(supcode==2){
            supcategoryname = "상품문의";
        }
        if(supcode==3){
            supcategoryname = "배송문의";
        }
        if(supcode==4){
            supcategoryname = "회원문의";
        }
        if(supcode==5){
            supcategoryname = "기타";
        }
        return supcategoryname;
    }   // tranceSupCa() end

    // 3. 처리상태 이름 변환
    public String convertSupState(int supstate){
        String supstatename = "";
        if(supstate==1){
            supstatename = "상담전";
        }
        if(supstate==2){
            supstatename = "진행중";
        }
        if(supstate==3){
            supstatename = "상담완료";
        }
        return supstatename;
    }   // convertSupState() end

}   // class end
