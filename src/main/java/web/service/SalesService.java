package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.dao.SalesDao;

import java.util.ArrayList;
import java.util.Map;

@Service
public class SalesService {

    @Autowired
    SalesDao salesDao;

    //연간 매출 조회
    public ArrayList<Map<String, String>> yearlySales(String year) {
        return salesDao.yearlySales(year);
    }

    //월간 매출 조회
    public ArrayList<Map<String, String>> monthlySales(String year, String month) {
        return salesDao.monthlySales(year, month);
    }




}
