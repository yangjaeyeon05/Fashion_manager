package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.EmailService;

@RestController
public class EmailController {

    @Autowired
    EmailService emailService;

    // * 이메일기능 테스트를 위한 임의 매핑 쓸때 주소값 바꾸기!!
    @GetMapping("/sendemail")
    public boolean emailSend(String toEmail , String subject , String content){
        return emailService.emailSend(toEmail ,subject , content);
    }   // sendEmail() end
}
