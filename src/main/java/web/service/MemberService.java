package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import web.model.dao.MemberDao;
import web.model.dto.MemberDto;
import web.model.dto.ProductDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    @Autowired
    MemberDao memberDao;

    // 회원목록 출력
    public List<MemberDto> memberPrint(){
        return memberDao.memberPrint();
    }

    // 회원 정보 수정(블랙리스트만)
    public boolean memberEdit(MemberDto memberDto){
        System.out.println(memberDto.getMemcode());
        System.out.println(memberDto.getBlacklist());
        return memberDao.memberEdit(memberDto);
    }

    // -------------------------- 2024-08-05 ---------------------------------------- //

    // 회원 번호에 맞는 사이즈 가져오기
    public String memberRecommend(MemberDto memberDto){
        String s = memberDao.memberRecommend(memberDto);
        System.out.println(s);
        return s;
    }

    // 가져온 회원의 선호 사이즈와 DAO 에서 받아온 정보를 반복문을 통해 사이즈가 일치하는 것만 새로운 LIST 에 저장함.
    public List<Map<String, String>> SizeRecommend(MemberDto memberDto){
        String s = memberRecommend(memberDto);
        memberDto.setMemsize(s);
        System.out.println(memberDto);
        List<ProductDto> list = memberDao.SizeRecommend();
        List<Map<String, String>> recommendList = new ArrayList<>();
        Map<String, String> recommend = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getProdSize().equals(memberDto.getMemsize())){
                recommend.put("size",list.get(i).getProdSize());
                recommend.put("color",String.valueOf(list.get(i).getColorCode()));
                recommend.put("name", list.get(i).getProdName());
                recommend.put("price",String.valueOf(list.get(i).getProdPrice()));
                recommend.put("desc",list.get(i).getProdDesc());
                recommend.put("file",list.get(i).getProdFilename());
                recommendList.add(recommend);
            }
        }
        System.out.println(recommendList);
        return recommendList;
    }


    // -------------------------- 2024-08-07 ---------------------------------------- //

}
