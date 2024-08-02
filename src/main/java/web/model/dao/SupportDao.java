package web.model.dao;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.dto.SupportDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Component
public class SupportDao extends Dao{

    // 1. 상담 목록 출력
    public ArrayList<SupportDto> supAllread(){
        System.out.println("SupportDao.supAllread");
        ArrayList<SupportDto> list = new ArrayList<>();
        try{
            String sql = "select * from support inner join members on members.memcode = support.memcode";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                SupportDto supportDto = SupportDto.builder()
                        .supcode(rs.getInt("supcode"))
                        .memcode(rs.getInt("memcode"))
                        .memname(rs.getString("memname"))
                        .supcategory(rs.getInt("supcategory"))
                        .suptitle(rs.getString("suptitle"))
                        .supdate(rs.getString("supdate"))
                        .supstate(rs.getInt("supstate"))
                        .build();
                list.add(supportDto);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return list;
    }   // supportDto() end

}   // class end
