package web.model.dao;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class FileDao extends Dao{

    PreparedStatement ps;
    @Autowired
    HttpServletRequest request;

    // [1] 엑셀로 현재 테이블 내보내기
    // 엑셀로 내보낼 ResultSet을 세션에 저장된 (가장 최근에 조회된 테이블의) SQL문을 사용해 생성하기
    public ResultSet exportToExcel(){
        System.out.println("ExcelDao");
        try {
            // 현재 세션에서 현재 페이지 테이블의 SQL문 가져오기 (currentSql = 매 조회시마다 저장되며 변경된다)
            // 세션에 저장된 모든 데이터는 Object형이므로 형변환
            HttpSession session = request.getSession();
            ps = (PreparedStatement) session.getAttribute("currentSql");
            System.out.println("Dao : " + ps);
            return ps.executeQuery(); // 조회된 ResultSet 리턴
        } catch (Exception e) {
            System.out.println("exportToExcel() : " + e);
        }
        return null;
    }
}
