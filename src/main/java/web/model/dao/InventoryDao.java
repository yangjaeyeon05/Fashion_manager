package web.model.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import web.model.dto.InventoryDto;
import web.model.dto.ProductDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryDao extends Dao{


    PreparedStatement ps;
    ResultSet rs;

    //  재고 목록 출력
    public List<ProductDto> inventoryRead(){
        List<ProductDto> list = new ArrayList<>();  // list 반환하기 위해 새로 선언
        try{
            String sql = "select p.prodname , prodgender , pd.prodsize , pd.proddetailcode ,  sum( pi.invlogchange) inv \n" +
                    "   from productdetail pd inner join product p  inner join invlog pi \n" +
                    "    on pd.prodcode = p.prodcode and pd.proddetailcode = pi.proddetailcode\n" +
                    "    group by pd.proddetailcode;";     // 제품 테이블과 제품 디테일 테이블 조인 후 출력
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(ProductDto.builder()                               // 빌더를 통해서 ProductDto 를 생성하고 list 에 바로 add 함
                        .prodName(rs.getString("prodname"))
                        .prodDetailcode(rs.getInt("proddetailcode"))
                        .prodGender(rs.getString("prodgender"))
                        .prodSize(rs.getString("prodsize"))
                        .prodAmount(rs.getInt("inv"))
                        .build());
            }
            System.out.println(list);
        }catch (Exception e){
            System.out.println("에러 정보는 " + e);
        }
        return list;                        // 저장된 list 반환
    }

    //  재고 현황 업데이트
    public boolean inventoryUpdate(InventoryDto inventoryDto){
        System.out.println("3번째 inventoryDto = " + inventoryDto);
        try{
            String sql = "insert into invlog(proddetailcode, invlogchange, invlogdetail) values (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,inventoryDto.getProddetailcode());
            ps.setInt(2,inventoryDto.getInvlogchange());
            ps.setInt(3,inventoryDto.getInvlogdetail());
            int count = ps.executeUpdate();
            if(count == 1){
                return true;
            }

        }catch (Exception e){
            System.out.println("에러 정보는 " + e);
        }
        return false;
    }

    // ===================================  2024-08-08 김민석 ========================================= //


    ////
    // ===================================  2024-08-12 김민석 ========================================= //
}
