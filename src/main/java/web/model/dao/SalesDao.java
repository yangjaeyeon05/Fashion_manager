package web.model.dao;

import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

@Component
public class SalesDao extends Dao{

    // 연도별 매출 조회
    public ArrayList<Map<String, String>> totalSales(String year) {
        try{
            // 총 매출 : 연도 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "SELECT YEAR(orddate) year, " +
                    "count(distinct ordcode) orders, " +
                    "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    "FROM orders o left outer join productdetail pd natural join orderdetail od natural join coupon c using(ordcode) " +
                    "where year(orddate) = ? " +
                    "group by YEAR(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(year));
            ResultSet rs = ps.executeQuery();

        }catch(Exception e){
            System.out.println("totalSales() : " + e );
        }
        return null;
    }

    // 톡정 연도 월 매출 조회
    public ArrayList<Map<String, String>> yearlySales(String year) {
        try{
            //연매출 : 월 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "Month(orddate) as month, " +
                         "count(distinct ordcode) orders, " +
                         "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                         "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                         "FROM orders o left outer join productdetail pd natural join orderdetail od natural join coupon c using(ordcode) " +
                         "where year(orddate) = ? " +
                         "group by YEAR(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(year));
            ResultSet rs = ps.executeQuery();


        }catch(Exception e){
            System.out.println("yearlySales() : " + e );
        }
        return null;
    }

    // 특정 연도 특정 월 일간 매출 조회
    public ArrayList<Map<String, String>> monthlySales(String year, String month) {
        try{
            //월간매출 : 날짜 / 월 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "SELECT DAY(orddate) as day, " +
                    "count(distinct ordcode) orders, " +
                    "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    "FROM orders o left outer join productdetail pd natural join orderdetail od natural join coupon c using(ordcode) " +
                    "where year(orddate) = ? and month(orddate) = ? " +
                    "group by YEAR(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(year)); ps.setInt(2, Integer.parseInt(month));
            ResultSet rs = ps.executeQuery();
        }catch(Exception e){
            System.out.println("monthlySales() : " + e );
        }
        return null;
    }

}
