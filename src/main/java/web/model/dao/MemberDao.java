package web.model.dao;


import org.springframework.stereotype.Component;
import web.model.dto.MemberDto;

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


}
