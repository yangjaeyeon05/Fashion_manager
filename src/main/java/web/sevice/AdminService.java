package web.sevice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import web.model.dao.AdminDao;
import web.model.dto.AdminDto;
import web.model.dto.MemberDto;

@Service
public class AdminService {

    @Autowired
    AdminDao adminDao;
    @Autowired
    HttpServletRequest request;  //  현재 요청을 보낸 클라이언트의 HTTP 요청 정보를 갖는 객체 주입

    // 관리자 로그인
    public boolean adminLogin(AdminDto adminDto){
        System.out.println(adminDto);
        int result = adminDao.adminLogin(adminDto);
        System.out.println("result = " + result);
        if(result > 0) {
            //  빌더 패턴 : 생성자가 아닌 메소드를 이용한 방식의 객체 생성
            AdminDto logInDto = AdminDto.builder().admincode(result).adminid(adminDto.getAdminid()).build();

            //  ======== HTTP 세션 처리 =========   //
            //  1. 현재 요청을 보내온 클라이언트의 세션 객체 호출
            HttpSession httpSession = request.getSession();

            //  2. 세션 객체에 속성 추가
            httpSession.setAttribute("logInDto", logInDto);
            return true;
        }
        return false;
    }

    // 관리자 로그인 상태 반환
    public AdminDto mLogInCheck(){
        //  1. 현재 요청을 보내온 클라이언트의 세션 객체 호출
        HttpSession session = request.getSession();

        //  2. 세션 객체 내 속성 값 호출, 타입 변환이 필수
        Object object = session.getAttribute("logInDto");

        //  3. 유효성 검사
        if(object != null){
            return (AdminDto) object;
        }
        return null;
    }
}
