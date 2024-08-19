package web.model.dao;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import web.model.dto.PologDto;
import web.model.dto.VendorDto;
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
                    " inner join product p on p.prodcode = pd.prodcode " +
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
                        .prodname(rs.getString("prodname"))
                        .build();
                // product 테이블이랑 select 재고까지 출력
                String sql2 = "select p.prodcode , sum( pi.invlogchange) inv " +
                        " from productdetail pd inner join product p  inner join invlog pi " +
                        " on pd.prodcode = p.prodcode and pd.proddetailcode = pi.proddetailcode " +
                        " group by pd.proddetailcode having proddetailcode = ?";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                // 기준은 위 sql 에서 뽑은 proddetailcode
                ps2.setInt(1 , rs.getInt("proddetailcode"));
                ResultSet rs2 = ps2.executeQuery();
                if(rs2.next()){
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
    public List<PologDto> poRead(int quantitystate){
        System.out.println("WholesaleProductDao.poRead");
        System.out.println("quantitystate = " + quantitystate);
        List<PologDto> list = new ArrayList<>();
        try{
            String sql = "select * from wholesaleproduct wp " +
                    " inner join vendor v on wp.vendorcode = v.vendorcode " +
                    " inner join productdetail pd on wp.proddetailcode = pd.proddetailcode " +
                    " inner join color c on pd.colorcode = c.colorcode " +
                    " inner join polog po on po.wpcode = wp.wpcode";
            if(quantitystate == 0){

            }else {
                sql += " where quantitystate = '"+quantitystate+"' ";
            }
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
            // 발주 후 상품이 도착하면 처리상태가 도착완료로 바뀌고 도착날짜에 버튼 누른 날짜 넣기
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
            // 발주 완료가 되면 재고로그테이블에 재고 입고가 됐다는 로그 넣기
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

    // 6. 거래처불러오기
    public List<VendorDto> vencoderead(){
        System.out.println("WholesaleProductDao.vencoderead");
        List<VendorDto> list = new ArrayList<>();
        try{
            String sql = "select * from vendor";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                VendorDto vendorDto = new VendorDto();
                vendorDto.setVendorcode(rs.getInt("vendorcode"));
                vendorDto.setVname(rs.getString("vname"));
                list.add(vendorDto);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return list;
    }   // vencoderead() end

    // 7. 도매상품 추가
    public boolean wpadd(WholesaleProductDto wpDto){
            // 필요한 필드 wpname , wpcost , proddetailcode , vendorode
        try{
            String sql = "insert into wholesaleproduct(wpname , wpcost , proddetailcode , vendorcode) values(? , ? , ? , ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1 , wpDto.getWpname());
            ps.setInt(2 , wpDto.getWpcost());
            ps.setInt(3 , wpDto.getProddetailcode());
            ps.setInt(4 , wpDto.getVendorcode());
            int count = ps.executeUpdate();
            if(count == 1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // wpadd() end

    // 8. 도매상품 삭제
    public boolean wpdelete(int wpcode){
        System.out.println("WholesaleProductDao.wpdelete");
        System.out.println("wpcode = " + wpcode);
        try{
            String sql = "delete from wholesaleproduct where wpcode = '"+wpcode+"'";
            PreparedStatement ps = conn.prepareStatement(sql);
            int count = ps.executeUpdate();
            if(count==1){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // wpdelete() end
}   // class end
