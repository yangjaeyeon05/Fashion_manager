package web.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    public boolean emailSend(String toEmail , String subject , String content){
        try{
            // 1. 메일 내용물들을 포맷/형식 맞추기 위해 MIME
            // 1. Mime 객체 생성
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            // 2. 메일 내용 구성
            // new MimeMessageHelper(Mime객체 , 첨부파일여부 , 인코딩타입)
            MimeMessageHelper mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true, "utf-8");
            // 3. 메일 보내는 사람
            mimeMessageHelper.setFrom("didwodus123@naver.com");
            // 4. 메일 받는 사람
            mimeMessageHelper.setTo(toEmail);
            // 5. 메일 제목
            mimeMessageHelper.setSubject(subject);
            // 6. 메일 내용
            mimeMessageHelper.setText(content);
            // 7. *** 메일 전송
            javaMailSender.send(mimeMessage);   // mime 객체 보내기
            return true;
        }catch (Exception e){
            System.out.println(e);
        }
        return false;
    }   // emailSend() end

}   // class end
