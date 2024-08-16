package web.model.dao;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import web.model.dto.OrderdetailDto;
import web.model.dto.PagenationDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Component

public class OrderDao extends Dao{

    //전체 주문 수를 반환하는 함수
    public int getTotalOrdersCount(int category , String firstdate,String todayDate){
        int count = 0;
        try{
            String sql = "Select COUNT(*) AS count From orderdetail inner join orders using (ordcode)"; //orderdetail의 레코드수 조회

            if (!firstdate.isEmpty() && !todayDate.isEmpty()) {
                sql = "Select COUNT(*) AS count From orderdetail inner join orders using (ordcode) WHERE orddate between '" + firstdate + "' and '" + todayDate + "'";
            }

            if ( category >= 1  ){sql = " Select COUNT(*) AS count From orderdetail inner join orders using (ordcode) where ordstate = " + category;}

            if (!firstdate.isEmpty() && !todayDate.isEmpty() && category >= 1) {
                sql = "Select COUNT(*) AS count From orderdetail inner join orders using (ordcode) WHERE orddate between '" + firstdate + "' and '" + todayDate + "'" + "and ordstate = " + category;
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                count = rs.getInt("count");
            }
        } catch (Exception e){System.out.println(e);} return count;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////
    //전체 주문 수를 반환하는 함수
    public int getTotalOrdersCount2( String firstdate,String todayDate){
        int count = 0;
        try{
            String sql = "Select COUNT(*) AS count From orderdetail inner join orders using (ordcode) where ordstate = 4"; //orderdetail의 레코드수 조회

            if (!firstdate.isEmpty() && !todayDate.isEmpty()) {
                sql = "Select COUNT(*) AS count From orderdetail inner join orders using (ordcode) WHERE orddate between '" + firstdate + "' and '" + todayDate + "' and ordstate = 4";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                count = rs.getInt("count");
            }
        } catch (Exception e){System.out.println(e);} return count;
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //주문목록 날짜 출력함수
    public ArrayList<OrderdetailDto> getorder(int category,int offset, int limit, String firstdate,String todayDate){ //function start
//        System.out.println("offset = " + offset);
//        System.out.println("limit = " + limit);
        ArrayList<OrderdetailDto> list = new ArrayList<>();
        try{
            String sql = "SELECT od.orddetailcode AS orddetailcode, m.memname AS memname,  o.orddate AS orddate, pd.prodsize AS prodsize, p.prodname AS prodname, od.ordamount AS ordamount, od.ordstate AS ordstate, c.coupname AS coupname FROM orders o JOIN orderdetail od ON o.ordcode = od.ordcode JOIN  productdetail pd ON od.proddetailcode = pd.proddetailcode JOIN product p ON pd.prodcode = p.prodcode LEFT JOIN  coupon c ON od.coupcode = c.coupcode JOIN members m ON o.memcode = m.memcode ";


            if (category == 0 && firstdate.isEmpty() ){}
            else {
                //조건 1 날짜가 존재하면
                if (!firstdate.isEmpty() && !todayDate.isEmpty()) {
                    sql += " WHERE orddate between '" + firstdate + "' and '" + todayDate + "'";
                }
                //조건 1-1 날짜가 없는 기본이면
                //조건 2 카테고리가 존재하면
                if (category >= 1) {
                    sql += " and ordstate = " + category ;
                }
            }
//            System.out.println("category = " + category);
            sql += " limit ? , ?";

//            System.out.println("sql = " + sql);

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(2,limit);
            ps.setInt(1,offset);
            ResultSet rs = ps.executeQuery();
//            System.out.println(sql);
            while(rs.next()){ //while start
                list.add(OrderdetailDto.builder()
                        .orddetailcode(rs.getInt("orddetailcode"))
                        .memname(rs.getString("memname"))
                        .orddate(rs.getString("orddate"))
                        .prodsize(rs.getString("prodsize"))
                        .prodname(rs.getString("prodname"))
                        .ordamount(rs.getInt("ordamount"))
                        .ordstate(rs.getInt("ordstate"))
                        .coupname(rs.getString("coupname"))
                        .build());
            } //while end

        } catch (Exception e) {System.out.println(e);} return list;
    } //function end
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //주문목록 취소 목록 출력함수
    public ArrayList<OrderdetailDto> ordCancel (int offset, int limit, String firstdate,String todayDate){ //function start
//        System.out.println("offset = " + offset);
//        System.out.println("limit = " + limit);
        ArrayList<OrderdetailDto> list = new ArrayList<>();
        try{
            String sql = "SELECT od.orddetailcode AS orddetailcode, m.memname AS memname,  o.orddate AS orddate, pd.prodsize AS prodsize, p.prodname AS prodname, od.ordamount AS ordamount, od.ordstate AS ordstate, c.coupname AS coupname FROM orders o JOIN orderdetail od ON o.ordcode = od.ordcode JOIN  productdetail pd ON od.proddetailcode = pd.proddetailcode JOIN product p ON pd.prodcode = p.prodcode LEFT JOIN  coupon c ON od.coupcode = c.coupcode JOIN members m ON o.memcode = m.memcode where ordstate = 4";

            if (!firstdate.isEmpty() && !todayDate.isEmpty()) {
                sql += " and orddate between '" + firstdate + "' and '" + todayDate + "'";
            }

//            System.out.println("category = " + category);
            sql += " limit ? , ?";

//            System.out.println("sql = " + sql);

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(2,limit);
            ps.setInt(1,offset);
            ResultSet rs = ps.executeQuery();
//            System.out.println(sql);
            while(rs.next()){ //while start
                list.add(OrderdetailDto.builder()
                        .orddetailcode(rs.getInt("orddetailcode"))
                        .memname(rs.getString("memname"))
                        .orddate(rs.getString("orddate"))
                        .prodsize(rs.getString("prodsize"))
                        .prodname(rs.getString("prodname"))
                        .ordamount(rs.getInt("ordamount"))
                        .ordstate(rs.getInt("ordstate"))
                        .coupname(rs.getString("coupname"))
                        .build());
            } //while end

        } catch (Exception e) {System.out.println(e);} return list;
    } //function end
///////////////////////////////////////////////////////////////////////////////////////////
    // 주문 취소 확정
    public boolean ordcheck (int orddetailcode){

        try{
            String sql = "update orderdetail SET ordstate = -4 where orddetailcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,orddetailcode);
            int count = ps.executeUpdate();
            if (count == 1 ){return true;}
        }catch (Exception e){System.out.println(e);}
        return false;
    }
///////////////////////////////////////////////////////////////////////
    //취소주문목록 출력함수
    public ArrayList<OrderdetailDto> cancelOrder (int offset, int limit){ //function start

        ArrayList<OrderdetailDto> list = new ArrayList<>();
        try{
            String sql = "SELECT od.orddetailcode AS orddetailcode, m.memname AS memname,  o.orddate AS orddate, pd.prodsize AS prodsize, p.prodname AS prodname, od.ordamount AS ordamount, od.ordstate AS ordstate, c.coupname AS coupname FROM orders o JOIN orderdetail od ON o.ordcode = od.ordcode JOIN  productdetail pd ON od.proddetailcode = pd.proddetailcode JOIN product p ON pd.prodcode = p.prodcode LEFT JOIN  coupon c ON od.coupcode = c.coupcode JOIN members m ON o.memcode = m.memcode where ordstate = -4 limit ?,? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,offset);
            ps.setInt(2,limit);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){ //while start
                list.add(OrderdetailDto.builder()
                        .orddetailcode(rs.getInt("orddetailcode"))
                        .memname(rs.getString("memname"))
                        .orddate(rs.getString("orddate"))
                        .prodsize(rs.getString("prodsize"))
                        .prodname(rs.getString("prodname"))
                        .ordamount(rs.getInt("ordamount"))
                        .ordstate(rs.getInt("ordstate"))
                        .coupname(rs.getString("coupname"))
                        .build());
            } //while end

        } catch (Exception e) {System.out.println(e);} return list;
    } //function end
////////////////////////////////////////////////////////////////////////////////////////////
//취소의 전체 완료 수를 반환하는 함수
public int getTotalOrdersCount3(){
    int count = 0;
    try{
        String sql = "Select COUNT(*) AS count From orderdetail where ordstate = -4"; //orderdetail의 레코드수 조회

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            count = rs.getInt("count");
        }
    } catch (Exception e){System.out.println(e);} return count;
}

///////////////////////////////////////////////////////////////////////////////////////////////
//반품목록
public ArrayList<OrderdetailDto> ordReturn (int offset, int limit, String firstdate,String todayDate){ //function start
//        System.out.println("offset = " + offset);
//        System.out.println("limit = " + limit);
    ArrayList<OrderdetailDto> list = new ArrayList<>();
    try{
        String sql = "SELECT od.orddetailcode AS orddetailcode, m.memname AS memname,  o.orddate AS orddate, pd.prodsize AS prodsize, p.prodname AS prodname, od.ordamount AS ordamount, od.ordstate AS ordstate, c.coupname AS coupname FROM orders o JOIN orderdetail od ON o.ordcode = od.ordcode JOIN  productdetail pd ON od.proddetailcode = pd.proddetailcode JOIN product p ON pd.prodcode = p.prodcode LEFT JOIN  coupon c ON od.coupcode = c.coupcode JOIN members m ON o.memcode = m.memcode where ordstate = 3";

        if (!firstdate.isEmpty() && !todayDate.isEmpty()) {
            sql += " and orddate between '" + firstdate + "' and '" + todayDate + "'";
        }

//            System.out.println("category = " + category);
        sql += " limit ? , ?";

//            System.out.println("sql = " + sql);

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(2,limit);
        ps.setInt(1,offset);
        ResultSet rs = ps.executeQuery();
//            System.out.println(sql);
        while(rs.next()){ //while start
            list.add(OrderdetailDto.builder()
                    .orddetailcode(rs.getInt("orddetailcode"))
                    .memname(rs.getString("memname"))
                    .orddate(rs.getString("orddate"))
                    .prodsize(rs.getString("prodsize"))
                    .prodname(rs.getString("prodname"))
                    .ordamount(rs.getInt("ordamount"))
                    .ordstate(rs.getInt("ordstate"))
                    .coupname(rs.getString("coupname"))
                    .build());
        } //while end

    } catch (Exception e) {System.out.println(e);} return list;
} //function end
    ///////////////////////////////////////////////////////////////////////////////////////////
    // 반품 확정
    public boolean returnCheck (int orddetailcode){

        try{
            String sql = "update orderdetail SET ordstate = -3 where orddetailcode = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,orddetailcode);
            int count = ps.executeUpdate();
            if (count == 1 ){return true;}
        }catch (Exception e){System.out.println(e);}
        return false;
    }
    ///////////////////////////////////////////////////////////////////////
    //반품완료 목록 호출 함수
    public ArrayList<OrderdetailDto> returnOrd (int offset, int limit){ //function start

        ArrayList<OrderdetailDto> list = new ArrayList<>();
        try{
            String sql = "SELECT od.orddetailcode AS orddetailcode, m.memname AS memname,  o.orddate AS orddate, pd.prodsize AS prodsize, p.prodname AS prodname, od.ordamount AS ordamount, od.ordstate AS ordstate, c.coupname AS coupname FROM orders o JOIN orderdetail od ON o.ordcode = od.ordcode JOIN  productdetail pd ON od.proddetailcode = pd.proddetailcode JOIN product p ON pd.prodcode = p.prodcode LEFT JOIN  coupon c ON od.coupcode = c.coupcode JOIN members m ON o.memcode = m.memcode where ordstate = -3 limit ?,? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,offset);
            ps.setInt(2,limit);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){ //while start
                list.add(OrderdetailDto.builder()
                        .orddetailcode(rs.getInt("orddetailcode"))
                        .memname(rs.getString("memname"))
                        .orddate(rs.getString("orddate"))
                        .prodsize(rs.getString("prodsize"))
                        .prodname(rs.getString("prodname"))
                        .ordamount(rs.getInt("ordamount"))
                        .ordstate(rs.getInt("ordstate"))
                        .coupname(rs.getString("coupname"))
                        .build());
            } //while end

        } catch (Exception e) {System.out.println(e);} return list;
    } //function end
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//반품의 완료 수를 반환하는 함수
public int getTotalOrdersCount4(){
    int count = 0;
    try{
        String sql = "Select COUNT(*) AS count From orderdetail where ordstate = -3"; //orderdetail의 레코드수 조회

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            count = rs.getInt("count");
        }
    } catch (Exception e){System.out.println(e);} return count;
}
/////////////////////////////////////////////////////////////////////////////////////////
//전체 주문 수를 반환하는 함수
public int getTotalOrdersCount5( String firstdate,String todayDate){
    int count = 0;
    try{
        String sql = "Select COUNT(*) AS count From orderdetail inner join orders using (ordcode) where ordstate = 4"; //orderdetail의 레코드수 조회

        if (!firstdate.isEmpty() && !todayDate.isEmpty()) {
            sql = "Select COUNT(*) AS count From orderdetail inner join orders using (ordcode) WHERE orddate between '" + firstdate + "' and '" + todayDate + "' and ordstate = 4";
        }

        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            count = rs.getInt("count");
        }
    } catch (Exception e){System.out.println(e);} return count;
}
} //class end
