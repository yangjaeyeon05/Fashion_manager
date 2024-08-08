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
            // 기간별 날짜 검색 조건
            if(supportSearchDto.getStartDate().isEmpty()) {  // 만약 시작하는 날짜가 없으면 -> 기간 설정 검색을 하지 않는다.

            }else {
                if(supportSearchDto.getSupcode() == 0 && supportSearchDto.getSupstate() == 0 && supportSearchDto.getSearchKeyword().isEmpty()){
                    // 기간 검색을 할건데 supcode , supstate , searchKeyword가 없으면
                    sql += " where ";
                }else {
                    sql += " and ";
                }
                sql += " supdate between " + "'"+supportSearchDto.getStartDate()+"'" + " and " + "'"+supportSearchDto.getEndDate()+"'";
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

    // 2. 상담 상세 내용 출력
    public SupportDto supRead(int supcode){
        System.out.println("SupportDao.supRead");
        System.out.println("supcode = " + supcode);
        try{
            String sql = "select * from support inner join members on support.memcode = members.memcode where supcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1 , supcode);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){      // 상담코드 , 문의유형 , 처리상태 , 작성자명 , 작성자이메일 , 작성자 연락처 , 주문번호 , 상품코드 , 문의내용 , 접수일 출력
                // 상담 상세 내용 객체 생성
                SupportDto supportDto = SupportDto.builder()
                        .supcode(rs.getInt("supcode"))
                        .supcategory(rs.getInt("supcategory"))
                        .supstate(rs.getInt("supstate"))
                        .memname(rs.getString("memname"))
                        .mememail(rs.getString("mememail"))
                        .memcontact(rs.getString("memcontact"))
                        .ordcode(rs.getInt("ordcode"))
                        .proddetailcode(rs.getInt("proddetailcode"))
                        .supcontent(rs.getString("supcontent"))
                        .supdate(rs.getString("supdate"))
                        .build();
                // 상품코드에 따른 상품이름 출력하기
                String sql2 = "select * from product inner join productdetail on product.prodcode = productdetail.prodcode where proddetailcode = ?;";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setInt(1 , rs.getInt("proddetailcode"));
                ResultSet rs2 = ps2.executeQuery();
                if(rs2.next()){
                    supportDto.setProdname(rs2.getString("prodname"));
                }
                return supportDto;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }   // supRead() end


}   // class end
