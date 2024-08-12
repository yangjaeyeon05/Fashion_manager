package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.model.dto.ReplyDto;
import web.model.dto.SupportDto;
import web.service.SupportService;
import web.model.dto.SupportSearchDto;
import web.service.SupportService;

import java.util.ArrayList;

@RestController
@RequestMapping("/support")
public class SupportController {
    @Autowired
    SupportService supportService;

    // 1. 상담 목록 출력
    @GetMapping("/allread")
    public ArrayList<SupportDto> supAllread(SupportSearchDto supportSearchDto){
        System.out.println("SupportController.supAllread");
        System.out.println("supportSearchDto = " + supportSearchDto);
        return supportService.supAllread(supportSearchDto);
    }   // supportDto() end

    // 2. 상담 상세 내용 출력 8/8 기능 구현
    @GetMapping("/read")
    public SupportDto supRead(int supcode){
        System.out.println("SupportController.supportDto");
        System.out.println("supcode = " + supcode);
        return supportService.supRead(supcode);
    }   // supRead() end

    // 3. 상담내용 상세 출력 내 답글 출력하기
    @GetMapping("/respread")
    public String replyRead(int supcode){
        System.out.println("SupportController.replyRead");
        System.out.println("supcode = " + supcode);
        return supportService.replyRead(supcode);
    }   // replyRead() end

    // 4. 답글달기
    @PostMapping("/respadd")
    public boolean respadd(@RequestBody ReplyDto replyDto){
        System.out.println("SupportController.respadd");
        System.out.println("replyDto = " + replyDto);
        return supportService.respadd(replyDto);
    }   // respadd() end

    // 5. 답변 등록 했을 때 처리상태 변경 상담 전 -> 진행 중
    @PutMapping("/edittoing")
    public boolean replyUpdateToing(int supcode){
        return supportService.replyUpdateToing(supcode);
    }   // replyUpdateToing() end

    // 6. 답변완료 했을 때 처리 상태 변경 진행중 -> 상담완료
    @PutMapping("/edittocom")
    public boolean replyUpdateTocom(int supcode){
        System.out.println("SupportController.replyUpdateTocom");
        System.out.println("supcode = " + supcode);
        return supportService.replyUpdateTocom(supcode);
    }   // replyUpdateTocom() end

    // 7. 답변삭제
    @DeleteMapping("/respedelete")
    public boolean replyDelete(int supcode){
        return supportService.replyDelete(supcode);
    }   // replyDelete() end

}   // class end
