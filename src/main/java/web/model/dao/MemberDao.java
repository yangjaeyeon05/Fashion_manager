package web.model.dao;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import web.model.dto.MemberDto;
import web.model.dto.ProductDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MemberDao extends Dao{

    PreparedStatement ps;
    ResultSet rs;


    // 회원목록 출력
    public List<MemberDto> memberPrint(){
        List<MemberDto> list = new ArrayList<>();
        try{
            String sql = "select *from members join color on members.memcolor = color.colorcode";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                list.add(MemberDto.builder()
                        .memcode(rs.getInt("memcode"))
                        .memname(rs.getString("memname"))
                        .memcontact(rs.getString("memcontact"))
                        .mememail(rs.getString("mememail"))
                        .memgender(rs.getString("memgender"))
                        .colorname(rs.getString("colorname"))
                        .memsize(rs.getString("memsize"))
                        .memjoindate(rs.getString("memjoindate"))
                        .memlastdate(rs.getString("memlastdate"))
                        .blacklist(rs.getInt("blacklist"))
                        .build());
            }
        }catch (Exception e){
            System.out.println("에러는 " + e);
        }
        return list;
    }

    // 회원 정보 수정(블랙리스트만)
    public boolean memberEdit(MemberDto memberDto){
        try{
            if(memberDto.getBlacklist() == 1){
                String sql = "update members set blacklist = 0 where memcode = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1,memberDto.getMemcode());
                int count = ps.executeUpdate();
                if(count == 1){
                    return true;
                }
            }else{
                String sql = "update members set blacklist = 1 where memcode = ?";
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
    public List<ProductDto> SizeRecommend(){
        List<ProductDto> list = new ArrayList<>();
        try{
            String sql = "select prodsize, colorcode, prodname, prodprice, proddesc, prodfilename, prodgender from product p inner join productdetail pd on p.prodcode = pd.prodcode";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(ProductDto.builder()
                        .prodSize(rs.getString("prodsize"))
                        .colorCode(rs.getInt("colorcode"))
                        .prodName(rs.getString("prodname"))
                        .prodPrice(rs.getInt("prodprice"))
                        .prodDesc(rs.getString("proddesc"))
                        .prodFilename(rs.getString("prodfilename"))
                        .prodGender(rs.getString("prodgender"))
                        .build());
            }
        }catch (Exception e){
            System.out.println("에러는 " + e);
        }
        return list;
    }


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
        List<Map<String, String>> list = new ArrayList<>();
        try{
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
                Map<String , String> map = new HashMap<>();
                map.put("prodname",rs.getString("prodname"));
                map.put("prodprice",String.valueOf(rs.getInt("prodprice")));
                map.put("prodgender",rs.getString("prodgender"));
                map.put("proddesc",rs.getString("proddesc"));
                map.put("prodsize",rs.getString("prodsize"));
                list.add(map);
            }

        }catch (Exception e){
            System.out.println("에러 정보는 " + e);
        }
        return list;
    }

    // ===================================  2024-08-16 김민석 ========================================= //

}
