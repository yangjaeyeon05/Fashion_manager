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
import java.util.HashMap;
import java.util.Map;

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
            // 최근 일주일 매출 : 날짜 / 주문건수 / 주문상품수 / 반품상품수 / 취소상품수 / 실주문상품수 / 총매출금액 / 할인금액 / 실매출금액
            String sql = "select day(orddate) 일자, " + // 주문날짜에서 일 부분 잘라서 구분
                    "count(ordcode) 주문건수, " + // 주문코드마다 한번씩 더하기 (orderdetail테이블에서 ordcode 하나에 상품이 여러개 있을 수 있다)
                    "sum(ordamount) 주문상품수, " + // 총 주문된 상품 수
                    "sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수, " + // 반품된 상품 수
                    "sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수, " + // 취소된 상품 수
                    "sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                    "sum(case when ordstate = 5 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    "sum(case when ordstate = 5 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    "sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    "from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) " +
                    "where orddate >= curdate() - interval 7 day " + // 최근 일주일간 내역, curdate() : 현재 날짜 e.g. 2024-08-08, interval 숫자 연/월/일 : 날짜 간격
                    "group by day(orddate) order by day(orddate);";
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
                        .ordered(rs.getInt("주문상품수"))
                        .returned(rs.getInt("반품상품수"))
                        .canceled(rs.getInt("취소상품수"))
                        .completed(rs.getInt("실주문상품수"))
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
                    " sum(ordamount) 주문상품수, " +
                    " sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수, " +
                    " sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수, " +
                    " sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    " from orderdetail left outer join orders using (ordcode) inner join coupon using (coupcode) " +
                    " group by year(orddate) order by year(orddate) desc;";
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
                        .ordered(rs.getInt("주문상품수"))
                        .returned(rs.getInt("반품상품수"))
                        .canceled(rs.getInt("취소상품수"))
                        .completed(rs.getInt("실주문상품수"))
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
                         " sum(ordamount) 주문상품수, " +
                         " sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수, " +
                         " sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수, " +
                         " sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                         " sum(case when ordstate = 5 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                         " sum(case when ordstate = 5 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                         " sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                         " from orderdetail left outer join orders using (ordcode) inner join coupon using (coupcode) " +
                         " where year(orddate) = ? " +
                         " group by month(orddate) order by month(orddate);";
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
                        .ordered(rs.getInt("주문상품수"))
                        .returned(rs.getInt("반품상품수"))
                        .canceled(rs.getInt("취소상품수"))
                        .completed(rs.getInt("실주문상품수"))
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
                    " sum(ordamount) 주문상품수, " + // 총 주문된 상품 수
                    " sum(case when ordstate = 3 then 1 else 0 end) 반품상품수, " +
                    " sum(case when ordstate = 4 then 1 else 0 end) 취소상품수, " +
                    " sum(case when ordstate = 5 then 1 else 0 end) 실주문상품수, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    " from orderdetail left outer join orders using(ordcode)inner join coupon using(coupcode) " +
                    " where year(orddate) = ? and month(orddate) = ? " +
                    " group by day(orddate) order by day(orddate);";
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
                        .ordered(rs.getInt("주문상품수"))
                        .returned(rs.getInt("반품상품수"))
                        .canceled(rs.getInt("취소상품수"))
                        .completed(rs.getInt("실주문상품수"))
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
                        "sum(ordamount) 주문상품수, " +
                        "sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수, " +
                        "sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수, " +
                        "sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                        "sum(case when ordstate = 5 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                        "sum(case when ordstate = 5 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                        "sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                        "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join productcategory using(prodcatecode) inner join coupon using(coupcode) " +
                        "group by 제품코드, 제품분류명 order by 실매출금액 desc;";
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
                        .ordered(rs.getInt("주문상품수"))
                        .returned(rs.getInt("반품상품수"))
                        .canceled(rs.getInt("취소상품수"))
                        .completed(rs.getInt("실주문상품수"))
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
                    "sum(ordamount) 주문상품수, " +
                    "sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수, " +
                    "sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수, " +
                    "sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                    "sum(case when ordstate = 5 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    "sum(case when ordstate = 5 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    "sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join productcategory using(prodcatecode) inner join coupon using(coupcode) " +
                    "where year(orddate) = ? " +
                    "group by 제품코드, 제품분류명 order by 실매출금액 desc;";
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
                        .ordered(rs.getInt("주문상품수"))
                        .returned(rs.getInt("반품상품수"))
                        .canceled(rs.getInt("취소상품수"))
                        .completed(rs.getInt("실주문상품수"))
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
                    "sum(ordamount) 주문상품수, " +
                    "sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수, " +
                    "sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수, " +
                    "sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                    "sum(case when ordstate = 5 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    "sum(case when ordstate = 5 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    "sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    "from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join productcategory using(prodcatecode) inner join coupon using(coupcode) " +
                    "where year(orddate) = ? and month(orddate) = ? " +
                    "group by prodcode, prodcatecode order by 실매출금액 desc;";
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
                        .ordered(rs.getInt("주문상품수"))
                        .returned(rs.getInt("반품상품수"))
                        .canceled(rs.getInt("취소상품수"))
                        .completed(rs.getInt("실주문상품수"))
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
            String sql = "select prodcode 제품코드, prodname 제품명, colorname 색상, " +
                    " sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                    " sum(case when prodsize = 'S' then ordamount else 0 end) S, " +
                    " sum(case when prodsize = 'M' then ordamount else 0 end) M, " +
                    " sum(case when prodsize = 'L' then ordamount else 0 end) L, " +
                    " sum(case when prodsize = 'XL' then ordamount else 0 end) XL, " +
                    " sum(case when prodsize = 'XXL' then ordamount else 0 end) XXL " +
                    " from orderdetail inner join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join color using(colorcode) " +
                    " where orddate between ? and ? and ordstate = 5 " +
                    " group by prodcode, colorcode order by prodcode, 실주문상품수 desc;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, startDate); ps.setString(2, endDate);

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .prodcode(rs.getInt("제품코드"))
                        .prodname(rs.getString("제품명"))
                        .colorname(rs.getString("색상"))
                        .completed(rs.getInt("실주문상품수"))
                        .build();

                // 사이즈 S~XXL까지 각각 담아서 DTO에 저장
                Map<String, Integer> colorSize = new HashMap<>();
                colorSize.put("S", rs.getInt(5));
                colorSize.put("M", rs.getInt(6));
                colorSize.put("L", rs.getInt(7));
                colorSize.put("XL", rs.getInt(8));
                colorSize.put("XXL", rs.getInt(9));

                dto.setColorsize(colorSize);

                colorSizeList.add(dto);
            }

            return colorSizeList; // DTO 목록 반환 = 조회된 정보 테이블
        } catch (Exception e) {
            System.out.println("colorSize() : " + e);
        }
        return null;
    }

    // [3] 쿠폰코드별 ( 연도 1 연도 2는 선택 )
    public ArrayList<SalesDto> coupons(String startDate, String endDate){
        try {
            ArrayList<SalesDto> couponList = new ArrayList<>();
            String sql = "select coupcode 쿠폰고유번호, coupname 쿠폰코드, coupsalerate \"쿠폰할인율(%)\", coupexpdate 쿠폰만료일, " +
                    " count(ordcode) 주문건수, " +
                    " sum(ordamount) 주문상품수, " +
                    " sum(case when ordstate = 3 then ordamount else 0 end) 반품상품수, " +
                    " sum(case when ordstate = 4 then ordamount else 0 end) 취소상품수, " +
                    " sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount, 0) else 0 end) 총매출금액, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount*(coupsalerate/100), 0) else 0 end) 할인금액, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    " from orderdetail left outer join orders using(ordcode) inner join coupon using(coupcode) ";
            if (!startDate.isEmpty() && !endDate.isEmpty()){
                sql += " where orddate between ? and ? ";
            }
            sql += " group by coupcode order by 실매출금액 desc;";

            PreparedStatement ps = conn.prepareStatement(sql);
            if (!startDate.isEmpty() && !endDate.isEmpty()) {
                ps.setString(1, startDate);
                ps.setString(2, endDate);
            }

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);


            ResultSet rs = ps.executeQuery();
            while(rs.next()){ // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .coupcode(rs.getString(1))
                        .coupname(rs.getString(2))
                        .coupsalerate(rs.getInt(3))
                        .coupexpdate(rs.getString(4))
                        .orders(rs.getInt("주문건수"))
                        .ordered(rs.getInt("주문상품수"))
                        .returned(rs.getInt("반품상품수"))
                        .canceled(rs.getInt("취소상품수"))
                        .completed(rs.getInt("실주문상품수"))
                        .revenue(rs.getInt("총매출금액"))
                        .saleAmount(rs.getInt("할인금액"))
                        .income(rs.getInt("실매출금액"))
                        .build();

                couponList.add(dto);
            }

            return couponList;
        } catch (Exception e) {
            System.out.println("coupons() : " + e);
        }
        return null;
    }

    // [4] 판매추이 (최근 2주간)
    public ArrayList<SalesDto> biweeklySales(){
        try {
            ArrayList<SalesDto> biWeeklyList = new ArrayList<>();
            String sql = "select prodcode 제품코드, prodname 제품명, prodprice 제품가격, " +
                    " sum(case when orddate between curdate() - interval 13 day and curdate() then ordamount else 0 end) 실주문상품수, " +
                    " sum(case when orddate = curdate() then ordamount else 0 end) 당일, " +
                    " sum(case when orddate = curdate() - interval 1 day then ordamount else 0 end) \"1일 전\", " +
                    " sum(case when orddate = curdate() - interval 2 day then ordamount else 0 end) \"2일 전\", " +
                    " sum(case when orddate = curdate() - interval 3 day then ordamount else 0 end) \"3일 전\", " +
                    " sum(case when orddate = curdate() - interval 4 day then ordamount else 0 end) \"4일 전\", " +
                    " sum(case when orddate = curdate() - interval 5 day then ordamount else 0 end) \"5일 전\", " +
                    " sum(case when orddate = curdate() - interval 6 day then ordamount else 0 end) \"6일 전\", " +
                    " sum(case when orddate = curdate() - interval 7 day then ordamount else 0 end) \"7일 전\", " +
                    " sum(case when orddate = curdate() - interval 8 day then ordamount else 0 end) \"8일 전\", " +
                    " sum(case when orddate = curdate() - interval 9 day then ordamount else 0 end) \"9일 전\", " +
                    " sum(case when orddate = curdate() - interval 10 day then ordamount else 0 end) \"10일 전\", " +
                    " sum(case when orddate = curdate() - interval 11 day then ordamount else 0 end) \"11일 전\", " +
                    " sum(case when orddate = curdate() - interval 12 day then ordamount else 0 end) \"12일 전\", " +
                    " sum(case when orddate = curdate() - interval 13 day then ordamount else 0 end) \"13일 전\"  " +
                    " from orderdetail inner join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join color using(colorcode)  " +
                    " group by prodcode order by prodcode;";
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
                        .completed(rs.getInt("실주문상품수"))
                        .build();

                // 당일 ~ 13일 전까지 HashMap 객체로 담아서 DTO에 저장
                Map<String, Integer> daysMap = new HashMap<>();
                daysMap.put("당일", rs.getInt(5));
                daysMap.put("1일 전", rs.getInt(6));
                daysMap.put("2일 전", rs.getInt(7));
                daysMap.put("3일 전", rs.getInt(8));
                daysMap.put("4일 전", rs.getInt(9));
                daysMap.put("5일 전", rs.getInt(10));
                daysMap.put("6일 전", rs.getInt(11));
                daysMap.put("7일 전", rs.getInt(12));
                daysMap.put("8일 전", rs.getInt(13));
                daysMap.put("9일 전", rs.getInt(14));
                daysMap.put("10일 전", rs.getInt(15));
                daysMap.put("11일 전", rs.getInt(16));
                daysMap.put("12일 전", rs.getInt(17));
                daysMap.put("13일 전", rs.getInt(18));

                dto.setBiweeklysales(daysMap);

                biWeeklyList.add(dto);
            }

            return biWeeklyList;
        } catch (Exception e) {
            System.out.println("biweeklySales() : " + e);
        }
        return null;
    }

    // [5] 대비기간매출, 날짜구간1 2000-00-00 ~ 2000-00-00 vs 날짜구간2 2000-00-00 ~ 2000-00-00
    public ArrayList<SalesDto> compareDates(String firstDateStart, String firstDateEnd, String secondDateStart, String secondDateEnd) {
        try {
            // 제품카테고리명 / [조회기간] 실매출1 / 판매수량 1 / [대비기간] 실매출2 / 판매수량 2 / [신장률] (실매출1-실매출2)/실매출2*100 / (판매수량1-판매수량2)/판매수량2*100
            ArrayList<SalesDto> compareDatesList = new ArrayList<>();
            String sql = "with table1 as (select prodcatename 제품분류명, " +
                    " sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    " from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join productcategory using(prodcatecode) inner join coupon using(coupcode) " +
                    " where orddate between ? and ? " +
                    " group by prodcatecode), " +
                    " table2 as( " +
                    " select prodcatename 제품분류명, " +
                    " sum(case when ordstate = 5 then ordamount else 0 end) 실주문상품수, " +
                    " sum(case when ordstate = 5 then round(ordprice*ordamount*(1-(coupsalerate/100)), 0) else 0 end) 실매출금액 " +
                    " from orderdetail left outer join orders using(ordcode) inner join productdetail using(proddetailcode) inner join product using(prodcode) inner join productcategory using(prodcatecode) inner join coupon using(coupcode) " +
                    " where orddate between ? and ? " +
                    " group by prodcatecode) " +
                    " select 제품분류명, table1.실주문상품수, table1.실매출금액, table2.실주문상품수, table2.실매출금액, " +
                    " round(((table1.실주문상품수-table2.실주문상품수)/table2.실주문상품수)*100, 2) 실주문상품수, " +
                    " round((table1.실매출금액-table2.실매출금액)/table2.실매출금액*100, 2) 실매출금액 " +
                    " from table1 inner join table2 using(제품분류명) " +
                    " order by 제품분류명;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, firstDateStart); ps.setString(2, firstDateEnd);
            ps.setString(3, secondDateStart); ps.setString(4, secondDateEnd);

            // 엑셀 내보내기시 필요 코드 2
            // PreparedStatement 세션에 등록/교체
            HttpSession session = request.getSession();
            session.setAttribute("currentSql", ps);


            ResultSet rs = ps.executeQuery();
            while(rs.next()) { // ResultSet 추출하기
                SalesDto dto = SalesDto.builder() // SalesDto 하나 = SQL 조회된 레코드 행 하나, Lombok Build 메서드
                        .prodcatename(rs.getString(1))
                        .firstcompleted(rs.getInt(2))
                        .firstincome(rs.getInt(3))
                        .secondcompleted(rs.getInt(4))
                        .secondincome(rs.getInt(5))
                        .completedcalc(rs.getFloat(6))
                        .incomecalc(rs.getFloat(7))
                        .build();

                compareDatesList.add(dto);
            }

            return compareDatesList;
        } catch (Exception e) {
            System.out.println("compareDates() : " + e);
        }
        return null;
    }
}