package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.model.dto.AdminDto;
import web.model.dto.MemberDto;
import web.sevice.AdminService;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    // 관리자 로그인
    @GetMapping("/admin/login")
    public boolean adminLogin(String id, String pw){
        System.out.println(id);
        System.out.println(pw);
        return adminService.adminLogin(AdminDto.builder().adminid(id).adminpw(pw).build());
    }

    //  관리자 로그인 체크
    @GetMapping("/admin/login/check")
    public AdminDto mLogInCheck(){
        return adminService.mLogInCheck();
    }
}
