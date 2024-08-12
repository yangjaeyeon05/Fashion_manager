package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.dao.SalesDao;
import web.model.dto.SalesDto;

import java.util.ArrayList;

@Service
public class SalesService {

    @Autowired
    private SalesDao salesDao;
    @Autowired
    private FileService fileService;

    // [0] 최근 일주일 매출 조회 (레코드 : 일 단위)
    public ArrayList<SalesDto> weeklySales() {
        return salesDao.weeklySales();
    }

    // [1] 총 매출 조회 (레코드 단위 : 연도)
    public ArrayList<SalesDto> totalSales() {
        return salesDao.totalSales();
    }

    // [2] 연매출 조회 (레코드 : 월 단위)
    public ArrayList<SalesDto> yearlySales(int year) {
        return salesDao.yearlySales(year);
    }

    // [3] 월간 매출 조회 (레코드 : 일 단위)
    public ArrayList<SalesDto> monthlySales(int year, int month) {

        return salesDao.monthlySales(year, month);
    }

    // [] 현재 테이블을 엑셀 파일로 내보내기

    // 연 판매 상품 목록

    // 2주간 판매 추이
}
