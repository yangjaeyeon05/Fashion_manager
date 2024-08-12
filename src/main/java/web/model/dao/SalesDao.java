package web.model.dao;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import web.model.dto.SalesDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Component
public class SalesDao extends Dao{

    // 엑셀로 내보낼 ResultSet 생성 SQL문 세션 등록
    @Autowired
    HttpSession session;

    // [0] 최근 일주일 매출 조회
    public ArrayList<SalesDto> weeklySales() {
        try{
            ArrayList<SalesDto> salesList = new ArrayList<>();
            // 최근 일주일 매출 : 날짜 / 주문건수 / 주문상품수 / 반품건수 / 취소건수 / 실주문상품수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select day(orddate) day, " + // 주문날짜에서 일 부분 잘라서 구분
                    "count(distinct ordcode) orders, " + // 주문코드마다 한번씩 더하기 (orderdetail테이블에서 ordcode 하나에 상품이 여러개 있을 수 있다)
                    "count(ordstate) ordered, " + // 총 주문된 상품 수
                    "sum(case when ordstate = 3 then 1 else 0 end) returned, " + // 반품된 상품 수
                    "sum(case when ordstate = 4 then 1 else 0 end) canceled, " + // 취소된 상품 수
                    "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    "from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) " +
                    "where orddate >= curdate() - interval 7 day" + // 최근 일주일간 내역, curdate() : 현재 날짜 e.g. 2024-08-08, interval 숫자 연/월/일 : 날짜 간격
                    "group by day(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);

            // PreparedStatement 세션에 등록/교체
            session.setAttribute("currentSql", ps);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .day(rs.getInt("day"))
                        .orders(rs.getInt("orders"))
                        .ordered(rs.getInt("ordered"))
                        .returned(rs.getInt("returned"))
                        .canceled(rs.getInt("canceled"))
                        .completed(rs.getInt("completed"))
                        .revenue(rs.getInt("revenue"))
                        .saleAmount(rs.getInt("saleAmount"))
                        .income(rs.getInt("income"))
                        .build();
                salesList.add(dto);
            }
            return salesList; // DTO 목록 반환 = 조회된 정보 테이블
        }catch(Exception e){
            System.out.println("todaySales() : " + e );
        }
        return null; // try/error 실패시 null 반환
    }

    // [1] 총 매출 조회 (레코드 단위 : 연도)
    public ArrayList<SalesDto> totalSales() {
        try{
            ArrayList<SalesDto> salesList = new ArrayList<>();
            // 총 매출 : 연도 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select year(orddate) year, " +
                    "count(distinct ordcode) orders, " +
                    "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    "from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) " +
                    "group by year(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);

            // PreparedStatement 세션에 등록/교체
            session.setAttribute("currentSql", ps);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .day(rs.getInt("day"))
                        .orders(rs.getInt("orders"))
                        .ordered(rs.getInt("ordered"))
                        .returned(rs.getInt("returned"))
                        .canceled(rs.getInt("canceled"))
                        .completed(rs.getInt("completed"))
                        .revenue(rs.getInt("revenue"))
                        .saleAmount(rs.getInt("saleAmount"))
                        .income(rs.getInt("income"))
                        .build();
                salesList.add(dto);
            }
            return salesList; // DTO 목록 반환 = 조회된 정보 테이블
        }catch(Exception e){
            System.out.println("totalSales() : " + e );
        }
        return null; // try/error 실패시 null 반환
    }

    // [2] 연매출 조회 (레코드 : 월 단위)
    public ArrayList<SalesDto> yearlySales(int year) {
        try{
            ArrayList<SalesDto> salesList = new ArrayList<>();
            //연매출 : 월 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select month(orddate) as month, " +
                         "count(distinct ordcode) orders, " +
                         "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                         "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                         "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                         "from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) " +
                         "where year(orddate) = ? " +
                         "group by MONTH(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, year);

            // PreparedStatement 세션에 등록/교체
            session.setAttribute("currentSql", ps);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .day(rs.getInt("day"))
                        .orders(rs.getInt("orders"))
                        .ordered(rs.getInt("ordered"))
                        .returned(rs.getInt("returned"))
                        .canceled(rs.getInt("canceled"))
                        .completed(rs.getInt("completed"))
                        .revenue(rs.getInt("revenue"))
                        .saleAmount(rs.getInt("saleAmount"))
                        .income(rs.getInt("income"))
                        .build();
                salesList.add(dto);
            }
            return salesList; // DTO 목록 반환 = 조회된 정보 테이블
        }catch(Exception e){
            System.out.println("yearlySales() : " + e );
        }
        return null; // try/error 실패시 null 반환
    }

    // [3] 월간 매출 조회 (레코드 : 일 단위)
    public ArrayList<SalesDto> monthlySales(int year, int month) {
        try{
            ArrayList<SalesDto> salesList = new ArrayList<>();
            //월간매출 : 날짜 / 월 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select day(orddate) as day, " +
                    "count(distinct ordcode) orders, " +
                    "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    "from orderdetail left outer join orders using(ordcode)inner join coupon using(coupcode) " +
                    "where year(orddate) = ? and month(orddate) = ? " +
                    "group by DAY(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, year); ps.setInt(2, month);

            // PreparedStatement 세션에 등록/교체
            session.setAttribute("currentSql", ps);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .day(rs.getInt("day"))
                        .orders(rs.getInt("orders"))
                        .ordered(rs.getInt("ordered"))
                        .returned(rs.getInt("returned"))
                        .canceled(rs.getInt("canceled"))
                        .completed(rs.getInt("completed"))
                        .revenue(rs.getInt("revenue"))
                        .saleAmount(rs.getInt("saleAmount"))
                        .income(rs.getInt("income"))
                        .build();
                salesList.add(dto);
            }
            return salesList; // DTO 목록 반환 = 조회된 정보 테이블
        }catch(Exception e){
            System.out.println("monthlySales() : " + e );
        }
        return null; // try/error 실패시 null 반환
    }

    // [4] 총 판매된 제품 순위
    public ArrayList<SalesDto> totalProduct() {
        try{
            ArrayList<SalesDto> productList = new ArrayList<>();
            //총판매제품순위 : (순위) 제품코드 / 제품명 / 제품가격 / 제품카테고리명 / 주문건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select prodcode, prodname, prodprice, prodcatename, " +
                        "count(ordstate) ordered, " +
                        "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                        "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                        "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                        "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                        "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                        "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                        "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join coupon using(coupcode) " +
                        "group by prodcode order by income desc;";
            PreparedStatement ps = conn.prepareStatement(sql);

            // PreparedStatement 세션에 등록/교체
            session.setAttribute("currentSql", ps);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .day(rs.getInt("day"))
                        .orders(rs.getInt("orders"))
                        .ordered(rs.getInt("ordered"))
                        .returned(rs.getInt("returned"))
                        .canceled(rs.getInt("canceled"))
                        .completed(rs.getInt("completed"))
                        .revenue(rs.getInt("revenue"))
                        .saleAmount(rs.getInt("saleAmount"))
                        .income(rs.getInt("income"))
                        .build();
                productList.add(dto);
            }
            return productList; // DTO 목록 반환 = 조회된 정보 테이블

        }catch(Exception e){
            System.out.println("yearlyProduct() : " + e );
        }
        return null;
    }
    // [5] 연간 판매된 제품 순위
    public ArrayList<SalesDto> yearlyProduct(String year) {
        try{
            //연간판매제품순위 : 순위 / 제품코드 / 제품명 / 제품카테고리명 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select prodcode, prodname, prodprice, " +
                    "count(ordstate) ordered, " +
                    "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join coupon using(coupcode) " +
                    "group by prodcode order by income desc;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(year));
            ResultSet rs = ps.executeQuery();


        }catch(Exception e){
            System.out.println("yearlyProduct() : " + e );
        }
        return null;
    }

    // [6] 월간 판매된 제품 순위
    public ArrayList<SalesDto> monthlyProduct(String year, String month) {
        try{
            //월간매출 : 날짜 / 월 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select prodcode, prodname, prodprice, " +
                    "count(ordstate) ordered, " +
                    "sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    "sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleAmount, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join coupon using(coupcode) " +
                    "group by prodcode order by income desc;";
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
