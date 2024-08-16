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

    // [0] (매출탭 기본페이지) 최근 일주일 매출 조회 (레코드 단위 : 일)
    public ArrayList<SalesDto> weeklySales(){
        return salesDao.weeklySales();
    }

    // [1] 연단위 매출 조회 (레코드 단위 : 연도)
    public ArrayList<SalesDto> totalSales(){
        return salesDao.totalSales();
    }

    // [1-1] 월단위 매출 조회 (레코드 단위 : 월)
    public ArrayList<SalesDto> yearlySales(int year){
        return salesDao.yearlySales(year);
    }

    // [1-2] 일단위 매출 조회 (레코드 : 일 단위)
    public ArrayList<SalesDto> monthlySales(int year, int month){
        return salesDao.monthlySales(year, month);
    }

    // [2] 연단위 판매된 제품 순위
    public ArrayList<SalesDto> totalProducts(){
        return salesDao.totalProducts();
    }

    // [2-1] 월단위 판매된 제품 순위
    public ArrayList<SalesDto> yearlyProducts(int year){
        return salesDao.yearlyProducts(year);
    }

    // [2-2] 일단위 판매된 제품 순위
    public ArrayList<SalesDto> monthlyProducts(int year, int month){
        return salesDao.monthlyProducts(year, month);
    }

    // [2-3] 색상 및 크기별 매출 현황, 날짜구간 2000-00-00 ~ 2000-00-00
    public ArrayList<SalesDto> colorSize (String startDate, String endDate){
        return salesDao.colorSize(startDate, endDate);
    }

    // [3] 쿠폰코드별
    public ArrayList<SalesDto> coupons(){
        return salesDao.coupons();
    }

    // [4] 판매추이 (최근 2주간)
    public ArrayList<SalesDto> biweeklySales(){
        return salesDao.biweeklySales();
    }

    // [5] 회원성향분석
    public ArrayList<SalesDto> memberPreferences(){
        return salesDao.memberPreferences();
    }

    // [6] 대비기간매출, 날짜구간1 2000-00-00 ~ 2000-00-00 vs 날짜구간2 2000-00-00 ~ 2000-00-00
    public ArrayList<ArrayList<SalesDto>> compareDates(String firstDate, String secondDate){
        return salesDao.compareDates(firstDate, secondDate);
    }


}
