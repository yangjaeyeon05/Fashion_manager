package web.model.dao;

import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

@Component
public class SalesDao extends Dao{
    // [0] 당일 매출 조회
    public ArrayList<Map<String, String>> todaySales() {
        try{
            // 오늘 매출 : 주문건수 / 주문상품수 / 반품건수 / 취소건수 / 실주문상품수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "SELECT DAY(orddate) day, " + // 레코드 단위 : 하루
                    "count(distinct ordcode) orders, " + // 주문코드마다 한번씩 더하기 (orderdetail테이블에서 ordcode 하나에 상품이 여러개 있을 수 있다)
                    "count(ordstate) ordered, " + // 총 주문된 상품 수
                    "sum(case when ordstate = 3 then 1 else 0 end) returned, " + // 반품된 상품 수
                    "sum(case when ordstate = 4 then 1 else 0 end) canceled, " + // 취소된 상품 수
                    "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    "FROM orders o left outer join productdetail pd natural join orderdetail od natural join coupon c using(ordcode) " +
                    "where orddate = curdate()" + // curdate() : 현재 날짜 e.g. 2024-08-08
                    "group by DAY(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

        }catch(Exception e){
            System.out.println("todaySales() : " + e );
        }
        return null;
    }

    // [1] 총 매출 조회 (레코드 단위 : 연도)
    public ArrayList<Map<String, String>> totalSales() {
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
                    "group by YEAR(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

        }catch(Exception e){
            System.out.println("totalSales() : " + e );
        }
        return null;
    }

    // [2] 연매출 조회 (레코드 : 월 단위)
    public ArrayList<Map<String, String>> yearlySales(String year) {
        try{
            //연매출 : 월 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "MONTH(orddate) as month, " +
                         "count(distinct ordcode) orders, " +
                         "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                         "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                         "FROM orders o left outer join productdetail pd natural join orderdetail od natural join coupon c using(ordcode) " +
                         "where year(orddate) = ? " +
                         "group by MONTH(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(year));
            ResultSet rs = ps.executeQuery();


        }catch(Exception e){
            System.out.println("yearlySales() : " + e );
        }
        return null;
    }

    // [3] 월간 매출 조회 (레코드 : 일 단위)
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
                    "group by DAY(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(year)); ps.setInt(2, Integer.parseInt(month));
            ResultSet rs = ps.executeQuery();
        }catch(Exception e){
            System.out.println("monthlySales() : " + e );
        }
        return null;
    }

    // [4] 총 판매된 제품 순위


    // [5] 연간 판매된 제품 순위
    public ArrayList<Map<String, String>> yearlyProduct(String year) {
        try{
            //연간판매제품순위 : 월 / 제품코드 / 제품명 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "MONTH(orddate) as month, " +
                    "count(distinct ordcode) orders, " +
                    "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    "FROM orders o left outer join productdetail pd natural join orderdetail od natural join coupon c using(ordcode) " +
                    "where year(orddate) = ? " +
                    "group by MONTH(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(year));
            ResultSet rs = ps.executeQuery();


        }catch(Exception e){
            System.out.println("yearlyProduct() : " + e );
        }
        return null;
    }

    // [6] 월간 판매된 제품 순위
    public ArrayList<Map<String, String>> monthlyProduct(String year, String month) {
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
                    "where YEAR(orddate) = ? and MONTH(orddate) = ? " +
                    "group by DAY(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(year)); ps.setInt(2, Integer.parseInt(month));
            ResultSet rs = ps.executeQuery();
        }catch(Exception e){
            System.out.println("monthlyProduct() : " + e );
        }
        return null;
    }
    // [7] 쿠폰코드별

    // [8] 판매추이

    // [9] 회원성향분석

    // [10] 대비기간매출? 날짜구간1 vs 날짜구간2

    // [11] 색상 및 크기별 매출 현황? 날짜구간

    // [7?] 현재 테이블을 엑셀 파일로 내보내기
}
