package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.model.dto.SupportDto;
import web.service.SupportService;

import java.util.ArrayList;

@RestController
@RequestMapping("/support")
public class SupportController {
    @Autowired
    SupportService supportService;

    // 1. 상담 목록 출력
    @GetMapping("/allread")
    public ArrayList<SupportDto> supAllread(){
        System.out.println("SupportController.supAllread");
        return supportService.supAllread();
    }   // supportDto() end

}   // class end
