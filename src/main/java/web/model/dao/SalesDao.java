package web.model.dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.dto.SalesDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@Component
public class SalesDao extends Dao{

    // 엑셀 내보내기시 필요코드 1
    // 엑셀로 내보낼 ResultSet 생성 SQL문 세션 등록
    // 관리자 로그인 시 생성된 HttpSession 사용
    @Autowired
    HttpServletRequest request;

//    // 엑셀 내보내기시 필요 코드 2 (Dao SQL PreparedStatement 생성 후 부분에 붙여넣기)
//    // PreparedStatement 세션에 등록/교체
//    HttpSession session = request.getSession();
//            session.setAttribute("currentSql", ps);



    // [0] (매출탭 기본페이지) 최근 일주일 매출 조회 (레코드 단위 : 일)
    public ArrayList<SalesDto> weeklySales() {
        try{
            ArrayList<SalesDto> salesList = new ArrayList<>();
            // 최근 일주일 매출 : 날짜 / 주문건수 / 주문상품수량 / 반품상품수량 / 취소상품수량 / 실주문상품수량 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select day(orddate) 일자, " + // 주문날짜에서 일 부분 잘라서 구분
                    "count(ordcode) 주문건수, " + // 주문코드마다 한번씩 더하기 (orderdetail테이블에서 ordcode 하나에 상품이 여러개 있을 수 있다)
                    "sum(ordamount) 총주문상품수, " + // 총 주문된 상품 수
                    "sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수량, " + // 반품된 상품 수
                    "sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수량, " + // 취소된 상품 수
                    "sum(case when ordstate !=3 and ordstate !=4 then ordamount else 0 end) 실주문상품수량, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    "from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) " +
                    "where orddate >= curdate() - interval 1 year " + // 최근 일주일간 내역, curdate() : 현재 날짜 e.g. 2024-08-08, interval 숫자 연/월/일 : 날짜 간격
                    "group by day(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .day(rs.getInt("일자"))
                        .orders(rs.getInt("주문건수"))
                        .ordered(rs.getInt("총주문상품수"))
                        .returned(rs.getInt("반품상품수량"))
                        .canceled(rs.getInt("취소상품수량"))
                        .completed(rs.getInt("실주문상품수량"))
                        .revenue(rs.getInt("총매출금액"))
                        .saleAmount(rs.getInt("할인금액"))
                        .income(rs.getInt("실매출금액"))
                        .build();
                salesList.add(dto);
            }
            return salesList; // DTO 목록 반환 = 조회된 정보 테이블
        }catch(Exception e){
            System.out.println("weeklySales() : " + e );
        }
        return null; // try/error 실패시 null 반환
    }

    // [1] 연단위 매출 조회 (레코드 단위 : 연도)
    public ArrayList<SalesDto> totalSales() {
        try{
            ArrayList<SalesDto> salesList = new ArrayList<>();
            // 총 매출 : 연도 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select year(orddate) 연도, " +
                    " count(ordcode) 주문건수, " +
                    " sum(ordamount) 주문상품수량, " +
                    " sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수량, " +
                    " sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수량, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then ordamount else 0 end) 실주문상품수량, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    " from orderdetail left outer join orders using (ordcode) inner join coupon using (coupcode) " +
                    " group by year(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);
            System.out.println("세션 : " + session.getAttribute("currentSql"));

            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .year(rs.getInt("연도"))
                        .orders(rs.getInt("주문건수"))
                        .ordered(rs.getInt("주문상품수량"))
                        .returned(rs.getInt("반품상품수량"))
                        .canceled(rs.getInt("취소상품수량"))
                        .completed(rs.getInt("실주문상품수량"))
                        .revenue(rs.getInt("총매출금액"))
                        .saleAmount(rs.getInt("할인금액"))
                        .income(rs.getInt("실매출금액"))
                        .build();
                salesList.add(dto);
            }
            return salesList; // DTO 목록 반환 = 조회된 정보 테이블
        }catch(Exception e){
            System.out.println("totalSales() : " + e );
        }
        return null; // try/error 실패시 null 반환
    }

    // [1-1] 월단위 매출 조회 (레코드 단위 : 월)
    public ArrayList<SalesDto> yearlySales(int year) {
        try{
            ArrayList<SalesDto> salesList = new ArrayList<>();
            //연매출 : 월 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = " select month(orddate) as 월, " +
                         " count(ordcode) 주문건수, " +
                         " sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수량, " +
                         " sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수량, " +
                         " sum(case when ordstate !=3 and ordstate !=4 then ordamount else 0 end) 실주문상품수량, " +
                         " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                         " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                         " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                         " from orderdetail left outer join orders using (ordcode) inner join coupon using (coupcode) " +
                         " where year(orddate) = ? " +
                         " group by month(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, year);

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);


            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .month(rs.getInt("월"))
                        .orders(rs.getInt("주문건수"))
                        .ordered(rs.getInt("총주문상품수"))
                        .returned(rs.getInt("반품상품수량"))
                        .canceled(rs.getInt("취소상품수량"))
                        .completed(rs.getInt("실주문상품수량"))
                        .revenue(rs.getInt("총매출금액"))
                        .saleAmount(rs.getInt("할인금액"))
                        .income(rs.getInt("실매출금액"))
                        .build();
                salesList.add(dto);
            }
            return salesList; // DTO 목록 반환 = 조회된 정보 테이블
        }catch(Exception e){
            System.out.println("yearlySales() : " + e );
        }
        return null; // try/error 실패시 null 반환
    }

    // [1-2] 일단위 매출 조회 (레코드 : 일 단위)
    public ArrayList<SalesDto> monthlySales(int year, int month) {
        try{
            ArrayList<SalesDto> salesList = new ArrayList<>();
            //월간매출 : 날짜 / 월 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = " select day(orddate) 일자, " +
                    " count(ordcode) 주문건수, " +
                    " sum(ordamount) 총주문상품수, " + // 총 주문된 상품 수
                    " sum(case when ordstate = 3 then 1 else 0 end) 반품상품수량, " +
                    " sum(case when ordstate = 4 then 1 else 0 end) 취소상품수량, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) 실주문상품수량, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    " from orderdetail left outer join orders using(ordcode)inner join coupon using(coupcode) " +
                    " where year(orddate) = ? and month(orddate) = ? " +
                    " group by day(orddate);";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, year); ps.setInt(2, month);

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .day(rs.getInt("일자"))
                        .orders(rs.getInt("주문건수"))
                        .ordered(rs.getInt("총주문상품수"))
                        .returned(rs.getInt("반품상품수량"))
                        .canceled(rs.getInt("취소상품수량"))
                        .completed(rs.getInt("실주문상품수량"))
                        .revenue(rs.getInt("총매출금액"))
                        .saleAmount(rs.getInt("할인금액"))
                        .income(rs.getInt("실매출금액"))
                        .build();
                salesList.add(dto);
            }
            return salesList; // DTO 목록 반환 = 조회된 정보 테이블
        }catch(Exception e){
            System.out.println("monthlySales() : " + e );
        }
        return null; // try/error 실패시 null 반환
    }

    // [2] 연단위 판매된 제품 순위
    public ArrayList<SalesDto> totalProducts() {
        try{
            ArrayList<SalesDto> productList = new ArrayList<>();
            //연간판매제품순위 : (순위) 제품코드 / 제품명 / 제품가격 / 제품카테고리명 / 주문건수 / 반품건수 / 취소건수 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select prodcode 제품코드, prodname 제품명, prodprice 제품가격, prodcatename 제품분류명, " +
                        "count(ordcode) 주문건수, " +
                        "sum(ordamount) 주문상품수량, " +
                        "sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수량, " +
                        "sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수량, " +
                        "sum(case when ordstate !=3 and ordstate !=4 then ordamount else 0 end) 실주문상품수량, " +
                        "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                        "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                        "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                        "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join coupon using(coupcode) " +
                        "group by prodcode, prodcatecode order by cast(replace(실매출금액, ',', '') as unsigned) desc;";
            PreparedStatement ps = conn.prepareStatement(sql);

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);


            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .prodcode(rs.getInt("제품코드"))
                        .prodname(rs.getString("제품명"))
                        .prodprice(rs.getInt("제품가격"))
                        .prodcatename(rs.getString("제품분류명"))
                        .orders(rs.getInt("주문건수"))
                        .ordered(rs.getInt("총주문상품수"))
                        .returned(rs.getInt("반품상품수량"))
                        .canceled(rs.getInt("취소상품수량"))
                        .completed(rs.getInt("실주문상품수량"))
                        .revenue(rs.getInt("총매출금액"))
                        .saleAmount(rs.getInt("할인금액"))
                        .income(rs.getInt("실매출금액"))
                        .build();
                productList.add(dto);
            }
            return productList; // DTO 목록 반환 = 조회된 정보 테이블

        }catch(Exception e){
            System.out.println("yearlyProduct() : " + e );
        }
        return null;
    }

    // [2-1] 월단위 판매된 제품 순위
    public ArrayList<SalesDto> yearlyProducts(int year) {
        try{
            ArrayList<SalesDto> productList = new ArrayList<>();
            //연간판매제품순위 : 순위 / 제품코드 / 제품명 / 제품카테고리명 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select prodcode 제품코드, prodname 제품명, prodprice 제품가격, prodcatename 제품분류명, " +
                    "count(ordcode) 주문건수, " +
                    "sum(ordamount) 주문상품수량, " +
                    "sum(case when ordstate = 3 then ordamount else 0 end) returned, " +
                    "sum(case when ordstate = 4 then ordamount else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then ordamount else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join coupon using(coupcode) " +
                    "where year(orddate) = ? " +
                    "group by prodcode, prodcatecode order by cast(replace(실매출금액, ',', '') as unsigned) desc;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, year);

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);


            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .prodcode(rs.getInt("제품코드"))
                        .prodname(rs.getString("제품명"))
                        .prodprice(rs.getInt("제품가격"))
                        .prodcatename(rs.getString("제품분류명"))
                        .orders(rs.getInt("주문건수"))
                        .ordered(rs.getInt("총주문상품수"))
                        .returned(rs.getInt("반품상품수량"))
                        .canceled(rs.getInt("취소상품수량"))
                        .completed(rs.getInt("실주문상품수량"))
                        .revenue(rs.getInt("총매출금액"))
                        .saleAmount(rs.getInt("할인금액"))
                        .income(rs.getInt("실매출금액"))
                        .build();
                productList.add(dto);
            }
            return productList; // DTO 목록 반환 = 조회된 정보 테이블

        }catch(Exception e){
            System.out.println("yearlyProduct() : " + e );
        }
        return null;
    }

    // [2-2] 일단위 판매된 제품 순위
    public ArrayList<SalesDto> monthlyProducts(int year, int month) {
        try{
            ArrayList<SalesDto> productList = new ArrayList<>();
            //월간판매제품순위 : 순위 / 제품코드 / 제품명 / 제품카테고리명 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select prodcode 제품코드, prodname 제품명, prodprice 제품가격, prodcatename 제품분류명, " +
                    "count(ordcode) 주문건수, " +
                    "sum(ordamount) 주문상품수량, " +
                    "sum(case when ordstate = 3 then ordamount else 0 end) returned, " +
                    "sum(case when ordstate = 4 then ordamount else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then ordamount else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join coupon using(coupcode) " +
                    "where year(orddate) = ? and month(orddate) = ? " +
                    "group by prodcode, prodcatecode order by cast(replace(실매출금액, ',', '') as unsigned) desc;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, year); ps.setInt(2, month);

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);


            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .prodcode(rs.getInt("제품코드"))
                        .prodname(rs.getString("제품명"))
                        .prodprice(rs.getInt("제품가격"))
                        .prodcatename(rs.getString("제품분류명"))
                        .orders(rs.getInt("주문건수"))
                        .ordered(rs.getInt("총주문상품수"))
                        .returned(rs.getInt("반품상품수량"))
                        .canceled(rs.getInt("취소상품수량"))
                        .completed(rs.getInt("실주문상품수량"))
                        .revenue(rs.getInt("총매출금액"))
                        .saleAmount(rs.getInt("할인금액"))
                        .income(rs.getInt("실매출금액"))
                        .build();
                productList.add(dto);
            }
            return productList; // DTO 목록 반환 = 조회된 정보 테이블
        }catch(Exception e){
            System.out.println("monthlyProduct() : " + e );
        }
        return null;
    }

    // [2-3] 색상 및 크기별 매출 현황, 날짜구간 2000-00-00 ~ 2000-00-00
    public ArrayList<SalesDto> colorSize (String startDate, String endDate){
        try {
            ArrayList<SalesDto> colorSizeList = new ArrayList<>();
            //색상 및 크기별 매출 현황 : 순위 / 제품코드 / 제품명 / 제품카테고리명 / 실주문건수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select prodcode 제품코드, prodname 제품명, prodprice 제품가격, prodcatename 제품분류명, " +
                    "count(ordcode) 주문건수, " +
                    "sum(ordamount) 주문상품수량, " +
                    "sum(case when ordstate = 3 then ordamount else 0 end) returned, " +
                    "sum(case when ordstate = 4 then ordamount else 0 end) canceled, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then ordamount else 0 end) completed, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    "sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join coupon using(coupcode) " +
                    "where year(orddate) = ? and month(orddate) = ? " +
                    "group by prodcode, prodcatecode order by cast(replace(실매출금액, ',', '') as unsigned) desc;";
            return colorSizeList;
        } catch (Exception e) {
            System.out.println("monthlyProduct() : " + e);
        }
        return null;
    }

    // [3] 쿠폰코드별
    public ArrayList<SalesDto> coupons(){
        try {
            ArrayList<SalesDto> couponList = new ArrayList<>();
            String sql = "select coupcode, coupname, coupsalerate, coupexpdate, " +
                    " count(distinct ordcode) orders, " +
                    " count(ordstate) ordered, " +
                    " sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    " sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleamount, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    " from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) " +
                    " group by coupcode order by income desc;";
            return couponList;
        } catch (Exception e) {
            System.out.println("monthlyProduct() : " + e);
        }
        return null;
    }

    // [4] 판매추이 (최근 2주간)
    public ArrayList<SalesDto> biweeklySales(){
        try {
            ArrayList<SalesDto> couponList = new ArrayList<>();
            String sql = "select coupcode, coupname, coupsalerate, coupexpdate, " +
                    " count(distinct ordcode) orders, " +
                    " count(ordstate) ordered, " +
                    " sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    " sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleamount, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    " from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) " +
                    " group by coupcode order by income desc;";
            return couponList;
        } catch (Exception e) {
            System.out.println("monthlyProduct() : " + e);
        }
        return null;
    }

    // [5] 회원성향분석
    public ArrayList<SalesDto> memberPreferences() {
        try {
            ArrayList<SalesDto> couponList = new ArrayList<>();
            String sql = "select coupcode, coupname, coupsalerate, coupexpdate, " +
                    " count(distinct ordcode) orders, " +
                    " count(ordstate) ordered, " +
                    " sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    " sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleamount, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    " from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) " +
                    " group by coupcode order by income desc;";
            return couponList;
        } catch (Exception e) {
            System.out.println("monthlyProduct() : " + e);
        }
        return null;
    }

    // [6] 대비기간매출, 날짜구간1 2000-00-00 ~ 2000-00-00 vs 날짜구간2 2000-00-00 ~ 2000-00-00
    public ArrayList<ArrayList<SalesDto>> compareDates(@RequestParam String firstDate, @RequestParam String secondDate){
        try {
            ArrayList<SalesDto> couponList = new ArrayList<>();
            String sql = "select coupcode, coupname, coupsalerate, coupexpdate, " +
                    " count(distinct ordcode) orders, " +
                    " count(ordstate) ordered, " +
                    " sum(case when ordstate = 3 then 1 else 0 end) returned, " +
                    " sum(case when ordstate = 4 then 1 else 0 end) canceled, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then 1 else 0 end) completed, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount, 0) else 0 end) revenue, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) saleamount, " +
                    " sum(case when ordstate !=3 and ordstate !=4 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) income " +
                    " from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) " +
                    " group by coupcode order by income desc;";
            return null;
        } catch (Exception e) {
            System.out.println("monthlyProduct() : " + e);
        }
        return null;
    }
}