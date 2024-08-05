package web.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.dao.MemberDao;
import web.model.dto.MemberDto;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    MemberDao memberDao;

    // 회원목록 출력
    public List<MemberDto> memberPrint(){
        return memberDao.memberPrint();
    }

    // 회원 정보 수정(블랙리스트만)
    public boolean memberEdit(MemberDto memberDto){
        return memberDao.memberEdit(memberDto);
    }
}
