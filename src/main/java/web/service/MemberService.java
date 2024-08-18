package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import web.model.dao.MemberDao;
import web.model.dto.MemberDto;
import web.model.dto.PagenationDto;
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
    public PagenationDto<MemberDto> memberPrint(PagenationDto pagenationDto){
        System.out.println("MemberService.memberPrint");
        System.out.println("pagenationDto = " + pagenationDto);

        // 매개변수는 없고 MemberDao 에서 LIST<ProductDto> 받아옴
        int offset = (pagenationDto.getPage() - 1) * pagenationDto.getSize();
        System.out.println("offset = " + offset);

        int memberCount = memberDao.memberCount(pagenationDto);
        System.out.println("memberCount = " + memberCount);

        int totalpages = memberCount % pagenationDto.getSize() == 0 ? (memberCount / pagenationDto.getSize()) : (memberCount / pagenationDto.getSize()) + 1;
        System.out.println("totalpages = " + totalpages);

        List<MemberDto> list = memberDao.memberPrint(pagenationDto, offset);
        System.out.println("list = " + list);

        return  PagenationDto.<MemberDto>builder()
                .page(pagenationDto.getPage())
                .size(pagenationDto.getSize())
                .totaldata(memberCount)
                .totalPage(totalpages)
                .data(list)
                .build();            // MemberDao 의 memberPrint 에서 받은 list 를 그대로 반환함
    }

    // 회원 정보 수정(블랙리스트만)
    public boolean memberEdit(MemberDto memberDto){     // 매개변수로 memberDto 를 받고
        System.out.println(memberDto.getMemcode());
        System.out.println(memberDto.getBlacklist());
        return memberDao.memberEdit(memberDto);         // memberDao 의 memberEdit 메소드에 memberDto 를 보내고 반환값을 그대로 반환함.
    }

    // -------------------------- 2024-08-05 ---------------------------------------- //

//    // 회원 번호에 맞는 사이즈 가져오기
//    public String memberRecommend(MemberDto memberDto){
//        //  DAO 의 memberRecommend 메소드에 memberDto 를 매개변수로 보내서 회원의 선호 사이즈를 String 타입으로 받아옴
//        String s = memberDao.memberRecommend(memberDto);
//        //  Sout 을 통해서 받은 값 확인
//        System.out.println(s);
//        //  회원의 선호 사이즈 반환
//        return s;
//    }
//
//    // 가져온 회원의 선호 사이즈와 DAO 에서 받아온 정보를 반복문을 통해 사이즈가 일치하는 것만 새로운 LIST 에 저장함.
//    public List<Map<String, String>> SizeRecommend(MemberDto memberDto){
//        String s = memberRecommend(memberDto);
//        memberDto.setMemsize(s);
//        System.out.println(memberDto);
//        List<ProductDto> list = memberDao.SizeRecommend();
//        List<Map<String, String>> recommendList = new ArrayList<>();
//        Map<String, String> recommend = new HashMap<>();
//        for (int i = 0; i < list.size(); i++) {
//            if(list.get(i).getProdSize().equals(memberDto.getMemsize())){
//                recommend.put("size",list.get(i).getProdSize());
//                recommend.put("color",String.valueOf(list.get(i).getColorCode()));
//                recommend.put("name", list.get(i).getProdName());
//                recommend.put("price",String.valueOf(list.get(i).getProdPrice()));
//                recommend.put("desc",list.get(i).getProdDesc());
//                recommend.put("file",list.get(i).getProdFilename());
//                recommendList.add(recommend);
//            }
//        }
//        System.out.println(recommendList);
//        return recommendList;
//    }


    // -------------------------- 2024-08-07 ---------------------------------------- //

    // 그 날 많이 팔린 물품 + 나의 성별 + 선호 사이즈에 따른 제품 추천

//    public List<Map<String, String>> memberRecommend2(MemberDto memberDto){
//
//        System.out.println("memberDto = " + memberDto);
//        List<ProductDto> list = memberDao.SizeRecommend(); // 제품목록
//        List<Map<String, String>> list1 = memberDao.memberRecommend2(memberDto); //
//        List<Map<String, String>> list2 = new ArrayList<>();
//
//        for (int i = 0; i < list1.size(); i++) {
//            System.out.println(">>");
//            System.out.println( list1.get(i).get("memgender"));
//            System.out.println( list1.get(i).get("memsize"));
//
//            for (int j = 0; j < list.size(); j++) {
//                System.out.println(">>>>>>");
//                System.out.println(list.get(j).getProdGender());
//                System.out.println(list.get(j).getProdSize());
//
//                if(list1.get(i).get("memgender").equals(list.get(j).getProdGender()) && list1.get(i).get("memsize").equals(list.get(j).getProdSize())){
//                    Map<String, String> map = new HashMap<>();
//                    map.put("prodname",list.get(j).getProdName());
//                    map.put("colorcode",String.valueOf(list.get(j).getColorCode()));
//                    map.put("prodsize",list.get(j).getProdSize());
//                    map.put("prodprice",String.valueOf(list.get(j).getProdPrice()));
//                    map.put("proddesc",list.get(j).getProdDesc());
//                    map.put("prodfilename",list.get(j).getProdFilename());
//                    map.put("prodgender",list.get(j).getProdGender());
//                    list2.add(map);
//                }
//            }
//
//        }
//        System.out.println("list1 = " + list1);
//        return list2;
//    }

    // 그 날 많이 팔린 물품 + 나의 성별 + 선호 사이즈에 따른 제품 추천
    public List<Map<String, String>> memberRecommend2(MemberDto memberDto) {    // 매개변수로 memberDto 를 받고
        return memberDao.memberRecommend2(memberDto);                           // memberDao 의 memberRecommend2 메소드에 memberDto 를 보내고 받은 List 을 그대로 반환함.
    }

    // ===================================  2024-08-16 김민석 ========================================= //
}
