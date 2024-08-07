package web.model.dao;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import web.model.dto.MemberDto;
import web.model.dto.ProductDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    // 회원 번호에 맞는 사이즈 먼저 가져오기
    public String memberRecommend(MemberDto memberDto){
        try{
            String sql = "select memsize from members where memcode = ?";
            System.out.println(sql);
            ps = conn.prepareStatement(sql);
            ps.setInt(1,memberDto.getMemcode());
            System.out.println(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                System.out.println(rs.getString("memsize"));
                return rs.getString("memsize");
            }
        }catch (Exception e){
            System.out.println("에러는 " + e);
        }
        return null;
    }


    // product 테이블과 productdetail 테이블을 join 해서 필요한 제품 정보 가져옴
    public List<ProductDto> SizeRecommend(){
        List<ProductDto> list = new ArrayList<>();
        try{
            String sql = "select prodsize, colorcode, prodname, prodprice, proddesc, prodfilename from product p inner join productdetail pd on p.prodcode = pd.prodcode";
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
                        .build());
            }
        }catch (Exception e){
            System.out.println("에러는 " + e);
        }
        return list;
    }


    // -------------------------- 2024-08-07 ---------------------------------------- //
}
