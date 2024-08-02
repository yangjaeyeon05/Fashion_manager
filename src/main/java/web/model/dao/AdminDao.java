package web.model.dao;

import org.springframework.stereotype.Component;
import web.model.dto.AdminDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Component
public class AdminDao extends Dao {

    PreparedStatement ps;
    ResultSet rs;

    // 관리자 로그인
    public int adminLogin(AdminDto adminDto){
        try{
            String sql = "select *from admin where adminid = ? and adminpw = ?";
            System.out.println("sql = " + sql);
            ps = conn.prepareStatement(sql);
            ps.setString(1,adminDto.getAdminid());
            ps.setString(2,adminDto.getAdminpw());
            System.out.println("sql = " + sql);
            rs = ps.executeQuery();
            System.out.println("rs = " + rs);
            if(rs.next()){
                return rs.getInt("admincode");
            }
        }catch (Exception e){
            System.out.println("에러는 " + e);
        }
        return 0;
    }
}
