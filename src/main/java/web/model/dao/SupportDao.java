package web.model.dao;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import web.model.dto.SupportDto;
import web.model.dto.SupportSearchDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Component
public class SupportDao extends Dao{

    // 1. 상담 목록 출력
    public ArrayList<SupportDto> supAllread(SupportSearchDto supportSearchDto){
        System.out.println("SupportDao.supAllread");
        System.out.println("supportSearchDto = " + supportSearchDto);
        ArrayList<SupportDto> list = new ArrayList<>();
        try{
            String sql = "select * from support inner join members on members.memcode = support.memcode ";
            // 문의 유형 조건
            if(supportSearchDto.getSupcode() >=1){  // supcode가 존재하면
                sql += " where supcode = " + supportSearchDto.getSupcode();
            }
            // 처리상태 조건
            if(supportSearchDto.getSupstate() == 0){    // supstate가 존재하지 않으면 그냥 넘어가기

            }else {                                     // supstate가 존재해서
                if(supportSearchDto.getSupcode() >=1){  // supcode가 있으면
                    sql += " and ";
                }else {                                 // supcode가 없으면
                    sql += " where ";
                }
                sql += " supstate = " + supportSearchDto.getSupstate();
            }
            // 검색조건
            if(supportSearchDto.getSearchKeyword().isEmpty()){  // 검색 조건이 없으면

            }else{                                              // 검색 조건이 있으면
                if(supportSearchDto.getSupcode() == 0 && supportSearchDto.getSupstate() == 0){  // 검색 조건이 있는데 supcode , supstate가 없으면
                    sql += " where ";
                }else{
                    sql += " and ";
                }
                sql += supportSearchDto.getSearchKey() + " like '%" + supportSearchDto.getSearchKeyword() + "%'";
            }
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
                        .ordcode(rs.getInt("ordcode"))
                        .build();
                list.add(supportDto);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return list;
    }   // supportDto() end


}   // class end
