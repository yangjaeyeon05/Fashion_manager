package web.model.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import web.model.dto.InventoryDto;
import web.model.dto.InventorySearhDto;
import web.model.dto.OrderdetailDto;
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
            // 제품 테이블과 제품 디테일 테이블, 재고 테이블 조인 후 sum(pi.invlogchange)를 통해 재고 수량 계산한 레코드도 함께 출력
            String sql = "select p.prodname , prodgender , pd.prodsize , pd.proddetailcode ,  sum( pi.invlogchange) inv \n" +
                    "   from productdetail pd inner join product p  inner join invlog pi \n" +
                    "    on pd.prodcode = p.prodcode and pd.proddetailcode = pi.proddetailcode\n" +
                    "    group by pd.proddetailcode;";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                list.add(ProductDto.builder()                               // 빌더를 통해서 ProductDto 를 생성하고 list 에 바로 add 함
                        .prodName(rs.getString("prodname"))
                        .prodDetailcode(rs.getInt("proddetailcode"))
                        .prodGender(rs.getString("prodgender"))
                        .prodSize(rs.getString("prodsize"))
                        .prodAmount(rs.getInt("inv"))       // 재고 수량 계산 값을 prodAmount 에 set 함
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
            String sql = "insert into invlog(proddetailcode, invlogchange, invlogdetail) values (?, ?, ?)";     // 재고 테이블에 재고 기록 추가
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

    public ProductDto inventoryAlarm(InventoryDto inventoryDto){
        System.out.println("inventoryDto = " + inventoryDto);
        try{
            //  특정한 레코드의 재고 수량만 출력하기 위해서 where 절 where pd.proddetailcode = ? 추가
            String sql = "select p.prodname , prodgender , pd.prodsize , pd.proddetailcode ,  sum( pi.invlogchange) inv \n" +
                    "   from productdetail pd inner join product p  inner join invlog pi \n" +
                    "    on pd.prodcode = p.prodcode and pd.proddetailcode = pi.proddetailcode \n" +
                    "    where pd.proddetailcode = ? \n" +
                    "    group by pd.proddetailcode";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,inventoryDto.getProddetailcode());

            rs = ps.executeQuery();

            if(rs.next()){
                // 레코드는 1줄만 출력될 것이기 때문에 if(rs.next()) 사용하고 빌더를 사용해서 ProductDto 생성하고 prodAmount 에 inv 레코드 값 저장하고 반환
                return ProductDto.builder().prodAmount(rs.getInt("inv")).build();
            }
        }catch (Exception e){
            System.out.println("에러 정보는 " + e);
        }
        return null;
    }

    // ===================================  2024-08-13 김민석 ========================================= //

    public List<InventoryDto> inventoryChart(InventoryDto inventoryDto){
        List<InventoryDto> list = new ArrayList<>();
        try{
            // 재고 수량 합계와 재고 입고 날짜를 group by i.invdate 를 통해서 날짜별로 묶어서 가져옴
            String sql = "select sum(i.invlogchange) sum, pd.proddetailcode, invdate from invlog i inner join productdetail pd on i.proddetailcode = pd.proddetailcode where pd.proddetailcode = ? group by i.invdate";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,inventoryDto.getProddetailcode());

            rs = ps.executeQuery();

            while (rs.next()){
                // 리스트에 InventoryDto 타입으로 재고 수량, 재고 입고 날짜 Set 함
                list.add(InventoryDto
                        .builder()
                        .prodAmount(rs.getInt("sum"))
                        .invdate(rs.getString("invdate"))
                        .build());
            }
        }catch (Exception e){
            System.out.println("에러 정보는 " + e);
        }
        return list;
    }

    // ===================================  2024-08-14 김민석 ========================================= //

//    // 주문 완료 시 주문 수량을 가져오는 메소드
//    public int invAutoUpdateOrder(InventoryDto inventoryDto){
//        System.out.println("inventoryDto = " + inventoryDto);
//        try{
//            String sql = "Select sum(ordamount) as sum From orderdetail where ordstate = 5 and proddetailcode = ?";
//
//            ps = conn.prepareStatement(sql);
//            ps.setInt(1,inventoryDto.getProddetailcode());
//
//            rs = ps.executeQuery();
//            if(rs.next()){
//                return rs.getInt("sum");
//            }
//
//        }catch (Exception e){
//            System.out.println("에러 정보는 " + e);
//        }
//        return 0;
//    }
//
//    // 주문 완료 시 자동 업데이트
//    public boolean invAutoUpdateOrder2(InventoryDto inventoryDto){
//        System.out.println("inventoryDto = " + inventoryDto);
//        try{
//            String sql = "insert into invlog(proddetailcode, invlogchange, invlogdetail) values (?, ?, 2)";     // 재고 테이블에 재고 기록 추가
//            ps = conn.prepareStatement(sql);
//            ps.setInt(1,inventoryDto.getProddetailcode());
//            ps.setInt(2,inventoryDto.getProdAmount());
//            int count = ps.executeUpdate();
//            if(count == 1){
//                return true;
//            }
//        }catch (Exception e){
//            System.out.println("에러는 " + e);
//        }
//        return false;
//    }

    // 주문 취소 시 취소 수량을 가져오는 메소드
    public InventoryDto invAutoUpdateCancel(OrderdetailDto orderdetailDto){
        System.out.println("orderdetailDto = " + orderdetailDto);
        try{
            String sql = "Select ordamount, proddetailcode From orderdetail where ordstate = -4 and orddetailcode = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1,orderdetailDto.getOrddetailcode());

            rs = ps.executeQuery();
            if(rs.next()){
                return InventoryDto
                        .builder()
                        .invlogchange(rs.getInt("ordamount"))
                        .proddetailcode(rs.getInt("proddetailcode"))
                        .build();
            }
        }catch (Exception e){
            System.out.println("에러 정보는 " + e);
        }
        return null;
    }

    // 주문 취소 시 자동 업데이트
    public boolean invAutoUpdateCancel2(InventoryDto inventoryDto){
        System.out.println("inventoryDto = " + inventoryDto);
        try{
            String sql = "insert into invlog(proddetailcode, invlogchange, invlogdetail) values (?, ?, 3)";     // 재고 테이블에 재고 기록 추가
            ps = conn.prepareStatement(sql);
            ps.setInt(1,inventoryDto.getProddetailcode());
            ps.setInt(2,inventoryDto.getInvlogchange());
            int count = ps.executeUpdate();
            if(count == 1){
                return true;
            }
        }catch (Exception e){
            System.out.println("에러는 " + e);
        }
        return false;
    }

    // 주문 반품 시 반품 수량을 가져오는 메소드
    public InventoryDto invAutoUpdateReturn(OrderdetailDto orderdetailDto){
        System.out.println("orderdetailDto = " + orderdetailDto);
        try{
            String sql = "Select ordamount, proddetailcode From orderdetail where ordstate = -3 and orddetailcode = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1,orderdetailDto.getOrddetailcode());

            rs = ps.executeQuery();
            if(rs.next()){
                return InventoryDto
                        .builder()
                        .invlogchange(rs.getInt("ordamount"))
                        .proddetailcode(rs.getInt("proddetailcode"))
                        .build();
            }
        }catch (Exception e){
            System.out.println("에러 정보는 " + e);
        }
        return null;
    }

    // 주문 반품 시 자동 업데이트
    public boolean invAutoUpdateReturn2(InventoryDto inventoryDto){
        System.out.println("inventoryDto = " + inventoryDto);
        try{
            String sql = "insert into invlog(proddetailcode, invlogchange, invlogdetail) values (?, ?, 4)";     // 재고 테이블에 재고 기록 추가
            ps = conn.prepareStatement(sql);
            ps.setInt(1,inventoryDto.getProddetailcode());
            ps.setInt(2,inventoryDto.getInvlogchange());
            int count = ps.executeUpdate();
            if(count == 1){
                return true;
            }
        }catch (Exception e){
            System.out.println("에러는 " + e);
        }
        return false;
    }

    // ===================================  2024-08-16 김민석 ========================================= //

    //  재고로그 출력 8/18 양재연
    public List<InventoryDto> invlogAllRead(InventorySearhDto inventorySearhDto){
        System.out.println("InventoryDao.invlogAllRead");
        List<InventoryDto> list = new ArrayList<>();
        try{
            String sql = "select * from invlog i " +
                    " inner join productdetail pd on i.proddetailcode = pd.proddetailcode " +
                    " inner join product p on p.prodcode = pd.prodcode " +
                    " inner join color c on c.colorcode = pd.colorcode ";
            // 날짜 검색 조건이 있으면
            if(inventorySearhDto.getStartDate().isEmpty()) {  // 만약 시작하는 날짜가 없으면 -> 기간 설정 검색을 하지 않는다.

            }else {
                sql += " where invdate between " + "'"+inventorySearhDto.getStartDate()+"'" + " and " + "'"+inventorySearhDto.getEndDate()+"' ";
            }
            // 증감사유 코드가 있을때
            if(inventorySearhDto.getInvlogdetail() == 0){

            }else {
                if(inventorySearhDto.getStartDate().isEmpty()){
                    sql += " where ";
                }else {
                    sql += " and ";
                }
                sql += " invlogdetail = '"+inventorySearhDto.getInvlogdetail()+"' ";
            }
            // 상세검색 조건이 있을때
            // 검색조건
            if(inventorySearhDto.getSearchKeyword().isEmpty()){  // 검색 조건이 없으면

            }else{                                              // 검색 조건이 있으면
                if(inventorySearhDto.getInvlogdetail() == 0 && inventorySearhDto.getStartDate().isEmpty()){  // 검색 조건이 있는데 supcode , supstate가 없으면
                    sql += " where ";
                }else{
                    sql += " and ";
                }
                sql += inventorySearhDto.getSearchKey() + " like '%" + inventorySearhDto.getSearchKeyword() + "%'";
            }
            sql += " order by invdate desc ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                InventoryDto inventoryDto = InventoryDto.builder()
                        .invlogcode(rs.getInt("invlogcode"))
                        .invdate(rs.getString("invdate"))
                        .invlogdetail(rs.getInt("invlogdetail"))
                        .prodname(rs.getString("prodname"))
                        .colorname(rs.getString("colorname"))
                        .prodsize(rs.getString("prodsize"))
                        .invlogchange(rs.getInt("invlogchange"))
                        .build();
                list.add(inventoryDto);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return list;
    }   // invlogAllRead() end
}
