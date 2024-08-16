package web.model.dao;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import web.model.dto.PologDto;
import web.model.dto.WholesaleProductDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class WholesaleProductDao extends Dao{

    // 1. 거래처별 상품 목록
    public List<WholesaleProductDto> wpRead(int vendorcode){
        System.out.println("WholesaleProductDao.wpRead");
        System.out.println("vendorcode = " + vendorcode);
        List<WholesaleProductDto> list = new ArrayList<>();
        try{
            String sql = "select * from wholesaleproduct wp " +
                    " inner join vendor v on wp.vendorcode = v.vendorcode " +
                    " inner join productdetail pd on wp.proddetailcode = pd.proddetailcode " +
                    " inner join color c on pd.colorcode = c.colorcode " +
                    " where v.vendorcode = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1 , vendorcode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                WholesaleProductDto wpdto = WholesaleProductDto.builder()
                        .wpcode(rs.getInt("wpcode"))
                        .wpname(rs.getString("wpname"))
                        .wpcost(rs.getInt("wpcost"))
                        .prodsize(rs.getString("prodsize"))
                        .colorname(rs.getString("colorname"))
                        .vname(rs.getString("vname"))
                        .build();
                // product 테이블이랑 select 재고까지 출력
                String sql2 = "select p.prodname , sum( pi.invlogchange) inv " +
                        " from productdetail pd inner join product p  inner join invlog pi " +
                        " on pd.prodcode = p.prodcode and pd.proddetailcode = pi.proddetailcode " +
                        " group by pd.proddetailcode having proddetailcode = ?";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setInt(1 , rs.getInt("proddetailcode"));
                ResultSet rs2 = ps2.executeQuery();
                if(rs2.next()){
                    wpdto.setProdname(rs2.getString("prodname"));
                    wpdto.setInv(rs2.getInt("inv"));
                }
                list.add(wpdto);
                System.out.println(list);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return list;
    }   // wpRead() end

    // 2. 발주
    public boolean doPo(PologDto pologDto){
        System.out.println("WholesaleProductDao.doPo");
        System.out.println("pologDto = " + pologDto);
        try{
            String sql = "insert into polog(wpcode , quantity , totalamount) values(? , ? , ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1 , pologDto.getWpcode());
            ps.setInt(2 , pologDto.getQuantity());
            ps.setInt(3 , pologDto.getTotalamount());
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // doPo() end

    // 3. 발주현황 출력
    public List<PologDto> poRead(){
        System.out.println("WholesaleProductDao.poRead");
        List<PologDto> list = new ArrayList<>();
        try{
            String sql = "select * from wholesaleproduct wp " +
                    " inner join vendor v on wp.vendorcode = v.vendorcode " +
                    " inner join productdetail pd on wp.proddetailcode = pd.proddetailcode " +
                    " inner join color c on pd.colorcode = c.colorcode " +
                    " inner join polog po on po.wpcode = wp.wpcode";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PologDto pologDto = PologDto.builder()
                        .pocode(rs.getInt("pocode"))
                        .wpname(rs.getString("wpname"))
                        .quantity(rs.getInt("quantity"))
                        .prodsize(rs.getString("prodsize"))
                        .colorname(rs.getString("colorname"))
                        .totalamount(rs.getInt("totalamount"))
                        .vname(rs.getString("vname"))
                        .quantitydate(rs.getString("quantitydate"))
                        .arrivaldate(rs.getString("arrivaldate"))
                        .quantitystate(rs.getInt("quantitystate"))
                        .proddetailcode(rs.getInt("proddetailcode"))
                        .build();
                list.add(pologDto);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return list;
    }   // poRead() end

    // 4. 발주 처리 상태 바꾸기
    public boolean poUpdate(int pocode){
        try{
            String sql = "update polog set quantitystate = 2 , arrivaldate = (current_date) where pocode = '"+pocode+"'";
            PreparedStatement ps = conn.prepareStatement(sql);
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // poUpdate() end

    // 5. 재고로그추가
    public boolean invlogadd(int proddetailcode , int quantity){
        try{
            String sql = "insert into invlog(proddetailcode, invlogchange, invlogdetail) values (?, ?, 1) ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,proddetailcode);
            ps.setInt(2,quantity);
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // invlogadd() end
}   // class end
