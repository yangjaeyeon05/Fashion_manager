package web.model.dao;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import web.model.dto.OrderdetailDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Component

public class OrderDao extends Dao{

    //주문목록 출력 함수
    public ArrayList<OrderdetailDto> getorder(){
        ArrayList<OrderdetailDto> list = new ArrayList<>();
        try{
            String sql = "SELECT od.orddetailcode AS orddetailcode, m.memname AS memname,  o.orddate AS orddate, pd.prodsize AS prodsize, p.prodname AS prodname, od.ordamount AS ordamount, od.ordstate AS ordstate, c.coupname AS coupname FROM orders o JOIN orderdetail od ON o.ordcode = od.ordcode JOIN  productdetail pd ON od.proddetailcode = pd.proddetailcode JOIN product p ON pd.prodcode = p.prodcode LEFT JOIN  coupon c ON od.coupcode = c.coupcode JOIN members m ON o.memcode = m.memcode";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
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
            }
        } catch (Exception e ) {System.out.println(e);} return list;
    } //함수 end

    //주문목록 날짜 출력함수
    public ArrayList<OrderdetailDto> getorderdate(String firstdate,String todayDate){
        ArrayList<OrderdetailDto> list = new ArrayList<>();
        try{
            String sql = "SELECT od.orddetailcode AS orddetailcode, m.memname AS memname,  o.orddate AS orddate, pd.prodsize AS prodsize, p.prodname AS prodname, od.ordamount AS ordamount, od.ordstate AS ordstate, c.coupname AS coupname FROM orders o JOIN orderdetail od ON o.ordcode = od.ordcode JOIN  productdetail pd ON od.proddetailcode = pd.proddetailcode JOIN product p ON pd.prodcode = p.prodcode LEFT JOIN  coupon c ON od.coupcode = c.coupcode JOIN members m ON o.memcode = m.memcode WHERE orddate between ? and ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, firstdate);
            ps.setString(2, todayDate);
            ResultSet rs = ps.executeQuery();
//            System.out.println(sql);
            while(rs.next()){
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
            }

        } catch (Exception e) {System.out.println(e);} return list;
    }

    //카테고리 목록 출력함수 0807 생성
    public ArrayList<OrderdetailDto> manage2 (int ordcatagory){
        ArrayList<OrderdetailDto> list = new ArrayList<>();
        try{
            String sql = "SELECT od.orddetailcode AS orddetailcode, m.memname AS memname,  o.orddate AS orddate, pd.prodsize AS prodsize, p.prodname AS prodname, od.ordamount AS ordamount, od.ordstate AS ordstate, c.coupname AS coupname FROM orders o JOIN orderdetail od ON o.ordcode = od.ordcode JOIN  productdetail pd ON od.proddetailcode = pd.proddetailcode JOIN product p ON pd.prodcode = p.prodcode LEFT JOIN  coupon c ON od.coupcode = c.coupcode JOIN members m ON o.memcode = m.memcode where ordstate = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,ordcatagory);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
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
            }
        } catch (Exception e) {System.out.println(e);} return list;
    }

} //class end
