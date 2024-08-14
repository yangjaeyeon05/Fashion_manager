package web.model.dao;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import web.model.dto.ReplyDto;
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
            if(supportSearchDto.getSupcategory() >=1){  // supcategory 존재하면
                sql += " where supcategory = " + supportSearchDto.getSupcategory();
            }
            // 처리상태 조건
            if(supportSearchDto.getSupstate() == 0){    // supstate가 존재하지 않으면 그냥 넘어가기

            }else {                                     // supcategory 존재해서
                if(supportSearchDto.getSupcategory() >=1){  // supcategory 있으면
                    sql += " and ";
                }else {                                 // supcategory 없으면
                    sql += " where ";
                }
                sql += " supstate = " + supportSearchDto.getSupstate();
            }
            // 검색조건
            if(supportSearchDto.getSearchKeyword().isEmpty()){  // 검색 조건이 없으면

            }else{                                              // 검색 조건이 있으면
                if(supportSearchDto.getSupcategory() == 0 && supportSearchDto.getSupstate() == 0){  // 검색 조건이 있는데 supcode , supstate가 없으면
                    sql += " where ";
                }else{
                    sql += " and ";
                }
                sql += supportSearchDto.getSearchKey() + " like '%" + supportSearchDto.getSearchKeyword() + "%'";
            }
            // 기간별 날짜 검색 조건
            if(supportSearchDto.getStartDate().isEmpty()) {  // 만약 시작하는 날짜가 없으면 -> 기간 설정 검색을 하지 않는다.

            }else {
                if(supportSearchDto.getSupcategory() == 0 && supportSearchDto.getSupstate() == 0 && supportSearchDto.getSearchKeyword().isEmpty()){
                    // 기간 검색을 할건데 supcode , supstate , searchKeyword가 없으면
                    sql += " where ";
                }else {
                    sql += " and ";
                }
                sql += " supdate between " + "'"+supportSearchDto.getStartDate()+"'" + " and " + "'"+supportSearchDto.getEndDate()+"'";
            }
            sql += " order by support.supcode desc";
            System.out.println(sql);
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
        SupportDto supportDto = null;

        try{
            String sql = "select * from support inner join members on support.memcode = members.memcode where supcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            System.out.println("sql = " + sql);
            ps.setInt(1 , supcode);
            System.out.println(supcode);
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            System.out.println(rs);
            if(rs.next()){      // 상담코드 , 문의유형 , 처리상태 , 작성자명 , 작성자이메일 , 작성자 연락처 , 주문번호 , 상품코드 , 문의내용 , 접수일 , 답변코드출력
                // System.out.println(rs.next()); 프린트로 next를 찍으면 다음행으로 한번 더 넘어가기 때문에 안됨..
                // 상담 상세 내용 객체 생성
                supportDto = SupportDto.builder()
                        .supcode(supcode)
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
                System.out.println("supportDto = " + supportDto);
                // 상품코드에 따른 상품이름 출력하기
                String sql2 = "select * from product inner join productdetail on product.prodcode = productdetail.prodcode where proddetailcode = ?;";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setInt(1 , rs.getInt("proddetailcode"));
                ResultSet rs2 = ps2.executeQuery();

                if(rs2.next()){
                    supportDto.setProdname(rs2.getString("prodname"));
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return supportDto;
    }   // supRead() end

    // 3. 상담내용 상세 출력 내 답글 출력하기
    public ReplyDto replyRead(int supcode){
        System.out.println("SupportDao.replyRead");
        System.out.println("supcode = " + supcode);
        ReplyDto replyDto = new ReplyDto();
        try{
            String sql = "select * from reply where supcode = '"+supcode+"'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                replyDto.setReplycode(rs.getInt("replycode"));
                replyDto.setReplycontent(rs.getString("replycontent"));
                System.out.println(replyDto);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return replyDto;
    }   // replyRead() end

    // 4. 답글달기
    public boolean respadd(ReplyDto replyDto){
        System.out.println("SupportDao.respadd");
        System.out.println("replyDto = " + replyDto);
        try{
            String sql = "insert into reply(supcode , replycontent) values( ? , ? )";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1 , replyDto.getSupcode());
            ps.setString(2 , replyDto.getReplycontent());
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // respadd() end

    // 5. 답변 등록 했을 때 처리상태 변경 상담 전 -> 진행 중
    public boolean replyUpdateToing(int supcode){
        try{
            String sql = "update support set supstate = 2 where supcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1 , supcode);
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // replyUpdateToing() end

    // 6. 답변완료 했을 때 처리 상태 변경 진행중 -> 상담완료
    public boolean replyUpdateTocom(int supcode){
        System.out.println("SupportDao.replyUpdateTocom");
        System.out.println("supcode = " + supcode);
        try{
            String sql = "update support set supstate = 3 where supcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1 , supcode);
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // replyUpdateTocom() end

    // 7. 답변삭제
    public boolean replyDelete(int supcode){
        try{
            String sql = "delete from reply where supcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1 , supcode);
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // replyDelete() end

    // 8. 답변수정
    public boolean replyUpdate(ReplyDto replyDto){
        System.out.println("SupportDao.replyUpdate");
        System.out.println("replyDto = " + replyDto);
        try{
            String sql = "update reply set replycontent = ? where replycode = ?"; // supcode를 받아서 그 조건으로 답변내용 수정하기
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1 , replyDto.getReplycontent());
            ps.setInt(2 , replyDto.getReplycode());
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // replyUpdate() end

}   // class end
