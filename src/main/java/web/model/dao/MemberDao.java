package web.model.dao;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import web.model.dto.MemberDto;
import web.model.dto.PagenationDto;
import web.model.dto.ProductDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@Component
public class MemberDao extends Dao{

    PreparedStatement ps;
    ResultSet rs;


    // 회원목록 출력
    public List<MemberDto> memberPrint(PagenationDto pagenationDto, int offset){
        List<MemberDto> list = new ArrayList<>();
        try{
            // 회원 테이블과 색상 테이블을 join 을 통해서 같이 조회해서 가져옴
            String sql = "select *from members join color on members.memcolor = color.colorcode";
            // 만약 pagenationDto 의 멤버변수 searchKey 에 저장된 값이 0이라면 아래 sql 구문 사용
            if(Objects.equals(pagenationDto.getSearchKey(), "0")){
                sql = "select *from members join color on members.memcolor = color.colorcode";
            }
            // 만약 searchKey 와 searchKeyword 에 저장된 값이 0이 아니고 다른 문자가 있다면 아래 sql 구문 사용
            if(!pagenationDto.getSearchKey().isEmpty() && !pagenationDto.getSearchKeyword().isEmpty()){
                sql = " select * from members join color on members.memcolor = color.colorcode where  " + pagenationDto.getSearchKey() + " like ? limit ?, ?";
                PreparedStatement ps2 = conn.prepareStatement(sql);
                ps2.setString(1,"%" + pagenationDto.getSearchKeyword() + "%");      // "% %" 사이의 문자를 집어넣음
                ps2.setInt(2,offset);                       // 1번째 ? 에 내가 몇 번째부터 출력할 것인지 알려주는 변수 offset 을 지정
                ps2.setInt(3,pagenationDto.getSize());      // 2번째 ? 에 내가 출력할 데이터의 갯수를 나타내는 pagenationDto 에 있는 멤버변수 size 에 저장되어 있는 값을 꺼내와서 지정
                rs = ps2.executeQuery();
                while (rs.next()){
                    list.add(MemberDto.builder()                                       // 리스트에 builder 를 통해서 MemberDto 를 바로 생성하고 add 함
                            .memcode(rs.getInt("memcode"))                  // 회원 번호
                            .memname(rs.getString("memname"))               // 회원 이름
                            .memcontact(rs.getString("memcontact"))         // 회원 전화번호
                            .mememail(rs.getString("mememail"))             // 회원 이메일
                            .memgender(rs.getString("memgender"))           // 회원 성별
                            .colorname(rs.getString("colorname"))           // 회원의 색상
                            .memsize(rs.getString("memsize"))               // 회원의 선호 사이즈
                            .memjoindate(rs.getString("memjoindate"))       // 회원 가입 날짜
                            .memlastdate(rs.getString("memlastdate"))       // 최근 접속 일자
                            .blacklist(rs.getInt("blacklist"))              // 블랙리스트 여부
                            .build());
                }
                return list;
            }

            sql += " limit ?, ?";
            System.out.println("sql = " + sql);
            ps = conn.prepareStatement(sql);
            ps.setInt(1,offset);                        // 1번째 ? 에 내가 몇 번째부터 출력할 것인지 알려주는 변수 offset 을 지정
            ps.setInt(2,pagenationDto.getSize());       // 2번째 ? 에 내가 출력할 데이터의 갯수를 나타내는 pagenationDto 에 있는 멤버변수 size 에 저장되어 있는 값을 꺼내와서 지정
            System.out.println("sql = " + sql);
            rs = ps.executeQuery();
            while (rs.next()){
                list.add(MemberDto.builder()                                       // 리스트에 builder 를 통해서 MemberDto 를 바로 생성하고 add 함
                        .memcode(rs.getInt("memcode"))                  // 회원 번호
                        .memname(rs.getString("memname"))               // 회원 이름
                        .memcontact(rs.getString("memcontact"))         // 회원 전화번호
                        .mememail(rs.getString("mememail"))             // 회원 이메일
                        .memgender(rs.getString("memgender"))           // 회원 성별
                        .colorname(rs.getString("colorname"))           // 회원의 색상
                        .memsize(rs.getString("memsize"))               // 회원의 선호 사이즈
                        .memjoindate(rs.getString("memjoindate"))       // 회원 가입 날짜
                        .memlastdate(rs.getString("memlastdate"))       // 최근 접속 일자
                        .blacklist(rs.getInt("blacklist"))              // 블랙리스트 여부
                        .build());
            }
        }catch (Exception e){
            System.out.println("에러는 " + e);
        }
        return list;
    }

    // 전체 회원 수 조회
    public int memberCount(PagenationDto pagenationDto){
        try{
            // 전체 회원의 숫자를 세는 sql 구문
            String sql = "select count(*) as count from members join color on members.memcolor = color.colorcode";
            // 만약 pagenationDto 의 멤버변수 searchKey 에 저장된 값이 0이라면 아래 sql 구문 사용
            if(Objects.equals(pagenationDto.getSearchKey(), "0")){
                sql = "select count(*) as count from members join color on members.memcolor = color.colorcode";
            }
            // 만약 searchKey 와 searchKeyword 에 저장된 값이 0이 아니고 다른 문자가 있다면 아래 sql 구문 사용
            if(!pagenationDto.getSearchKey().isEmpty() && !pagenationDto.getSearchKeyword().isEmpty()){
                sql = " select count(*) as count from members join color on members.memcolor = color.colorcode where   " + pagenationDto.getSearchKey() + " like  ?";
                PreparedStatement ps2 = conn.prepareStatement(sql);
                ps2.setString(1,"%" + pagenationDto.getSearchKeyword() + "%");  // "% %" 사이의 문자를 집어넣음
                rs = ps2.executeQuery();
                if(rs.next()){
                    return rs.getInt("count");
                }
            }
            System.out.println("sql = " + sql);
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            if(rs.next()){
                return rs.getInt("count");
            }

        }catch (Exception e){
            System.out.println("에러 정보는 " + e);
        }
        return 0;
    }

    // 회원 정보 수정(블랙리스트만)
    public boolean memberEdit(MemberDto memberDto){
        try{
            if(memberDto.getBlacklist() == 1){                                          // memberDto 에 저장된 블랙리스트 여부가 O 라면
                String sql = "update members set blacklist = 0 where memcode = ?";      // 특정 회원 번호의 블랙리스트 여부를 X로 바꿈
                ps = conn.prepareStatement(sql);
                ps.setInt(1,memberDto.getMemcode());
                int count = ps.executeUpdate();
                if(count == 1){
                    return true;
                }
            }else{                                                                      // memberDto 에 저장된 블랙리스트 여부가 X 라면
                String sql = "update members set blacklist = 1 where memcode = ?";      // 특정 회원 번호의 블랙리스트 여부를 O로 바꿈
                ps = conn.prepareStatement(sql);
                ps.setInt(1,memberDto.getMemcode());
                int count = ps.executeUpdate();
                if(count == 1){
                    return true;
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }


    // -------------------------- 2024-08-05 ---------------------------------------- //

//    // 회원 번호에 맞는 사이즈 먼저 가져오기
//    public String memberRecommend(MemberDto memberDto){
//        try{
//            String sql = "select memsize from members where memcode = ?";
//            System.out.println(sql);
//            ps = conn.prepareStatement(sql);
//            ps.setInt(1,memberDto.getMemcode());
//            System.out.println(sql);
//            rs = ps.executeQuery();
//            if(rs.next()){
//                System.out.println(rs.getString("memsize"));
//                return rs.getString("memsize");
//            }
//        }catch (Exception e){
//            System.out.println("에러는 " + e);
//        }
//        return null;
//    }


    // product 테이블과 productdetail 테이블을 join 해서 필요한 제품 정보 가져옴
//    public List<ProductDto> SizeRecommend(){
//        List<ProductDto> list = new ArrayList<>();
//        try{
//            String sql = "select prodsize, colorcode, prodname, prodprice, proddesc, prodfilename, prodgender from product p inner join productdetail pd on p.prodcode = pd.prodcode";
//            ps = conn.prepareStatement(sql);
//            rs = ps.executeQuery();
//            while(rs.next()){
//                list.add(ProductDto.builder()
//                        .prodSize(rs.getString("prodsize"))
//                        .colorCode(rs.getInt("colorcode"))
//                        .prodName(rs.getString("prodname"))
//                        .prodPrice(rs.getInt("prodprice"))
//                        .prodDesc(rs.getString("proddesc"))
//                        .prodFilename(rs.getString("prodfilename"))
//                        .prodGender(rs.getString("prodgender"))
//                        .build());
//            }
//        }catch (Exception e){
//            System.out.println("에러는 " + e);
//        }
//        return list;
//    }


    // -------------------------- 2024-08-07 ---------------------------------------- //

    // 그 날 많이 팔린 물품 + 나의 성별 + 선호 사이즈에 따른 제품 추천

//    public List<Map<String, String>> memberRecommend2(MemberDto memberDto){
//        List<Map<String, String>> list = new ArrayList<>();
//
//        System.out.println("memberDto = " + memberDto);
//        try{
//            String sql = "select * from\n" +
//                    "( select dayofweek(od.orddate) dow, sum(odd.ordamount) sum, od.ordcode\n" +
//                    "from orders od \n" +
//                    "inner join orderdetail odd \n" +
//                    "on od.ordcode = odd.ordcode \n" +
//                    "inner join members m\n" +
//                    "on od.memcode = m.memcode\n" +
//                    "where m.memcode != ? \n" +
//                    "and dayofweek(od.orddate) = dayofweek(now())\n" +
//                    "group by od.ordcode \n" +
//                    "order by sum desc ) as query1  , members where memcode = ? limit 5";
//
//            ps = conn.prepareStatement(sql);
//            ps.setInt(1,memberDto.getMemcode());
//            ps.setInt(2,memberDto.getMemcode());
//
//            rs = ps.executeQuery();
//
//            while (rs.next()){
//                Map<String , String> map = new HashMap<>();
//                map.put("dow",String.valueOf(rs.getInt("dow")));
//                map.put("sum",String.valueOf(rs.getInt("sum")));
//                map.put("memgender",rs.getString("memgender"));
//                map.put("memsize",rs.getString("memsize"));
//                list.add(map);
//            }
//        }catch (Exception e){
//            System.out.println("에러 정보는 " + e);
//        }
//
//        return list;
//    }
    public List<Map<String, String>> memberRecommend2(MemberDto memberDto) {
        List<Map<String, String>> list = new ArrayList<>();                 // 리스트 선언
        try{
            // 주문 테이블에서 주문 상세 테이블과 회원 테이블을 join 후 주문 날짜를 요일로 계산한 것과 주문 수량을 합친 것, 주문 코드를 조회하는데
            // 조건으로 특정한 회원이 주문한 것이 아니어야하며 오늘 날짜의 요일과 주문 날짜의 요일이 같은것만 조회함.
            // 서브쿼리 1 : 위의 조회한 것을 바탕으로 주문 상세 테이블과 제품 테이블, 제품 상세 테이블을 join 후 제품 테이블의 모든 것, 제품 상세 테이블의 제품 사이즈를 조회함.
            // 서브쿼리 2 : 서브쿼리 1에서 조회한 것을 바탕으로 제품 테이블의 제품 성별, 제품 사이즈와
            //            특정한 회원 번호(주문한 회원의 번호를 뜻함)를 조건으로 하는 회원 테이블을 조회해서 나온 특정한 회원 번호의 성별과 선호 사이즈가 같다는 조건으로
            //            나온 특정한 제품의 테이블의 모든 필드값을 조회함.
            String sql = "select query2.* from (\n" +
                    "\tselect p.* , pt.prodsize   from (  \n" +
                    "\t\t# 요일별 주문수량 순위\n" +
                    "\t\tselect dayofweek(od.orddate) dow, sum(odd.ordamount) sum, od.ordcode \n" +
                    "\t\tfrom orders od \n" +
                    "\t\tinner join orderdetail odd \n" +
                    "\t\ton od.ordcode = odd.ordcode \n" +
                    "\t\tinner join members m\n" +
                    "\t\ton od.memcode = m.memcode\n" +
                    "\t\twhere m.memcode != ? \n" +
                    "\t\tand dayofweek(od.orddate) = dayofweek(now())\n" +
                    "\t\tgroup by od.ordcode \n" +
                    "\t\torder by sum desc ) as query1 \n" +
                    "\t# 순위별 제품정보 \n" +
                    "\tinner join orderdetail od inner join product p inner join productdetail pt\n" +
                    "\ton query1.ordcode = od.ordcode and pt.proddetailcode = od.proddetailcode and pt.prodcode = p.prodcode\n" +
                    ") query2 , ( select * from members where memcode = ? ) m where prodgender = m.memgender and prodsize = m.memsize limit 0 , 5";

            ps = conn.prepareStatement(sql);
            ps.setInt(1,memberDto.getMemcode());
            ps.setInt(2,memberDto.getMemcode());

            rs = ps.executeQuery();
            while (rs.next()){
                Map<String , String> map = new HashMap<>();                                // DTO 에 따로 저장하지 않고 HashMap 의 key 와 value 를 이용해서 이름과 값을 지정해줌.
                map.put("prodname",rs.getString("prodname"));                   // key = "prodname" , value = "prodname"의 필드값
                map.put("prodprice",String.valueOf(rs.getInt("prodprice")));    // key = "prodprice", value = "prodprice"의 필드값(가격은 int이기 때문에 String.valueOf()를 통해서 문자열로 바꿔줌
                map.put("prodgender",rs.getString("prodgender"));               // key = "prodgender", value = "prodgender"의 필드값
                map.put("proddesc",rs.getString("proddesc"));                   // key = "proddesc", value = "proddesc"의 필드값
                map.put("prodsize",rs.getString("prodsize"));                   // key = "prodsize", value = "prodsize"의 필드값
                list.add(map);                                                             // key 와 value 가 저장된 HashMap 을 list 에 add 함.
            }

        }catch (Exception e){
            System.out.println("에러 정보는 " + e);
        }
        return list;
    }

    // ===================================  2024-08-16 김민석 ========================================= //

}
