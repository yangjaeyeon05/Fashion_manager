package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.model.dao.SalesDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SalesService {

    @Autowired
    SalesDao salesDao;
    @Autowired
    FileService fileService;

    //연간 매출 조회
    public ArrayList<Map<String, String>> yearlySales(String year) {
        return salesDao.yearlySales(year);
    }

    //월간 매출 조회
    public ArrayList<Map<String, String>> monthlySales(String year, String month) {
        return salesDao.monthlySales(year, month);
    }

    public boolean importExcel(MultipartFile excel) {
        String fileName = fileService.fileUpload(excel);
        List<Map<String, String>> excelData = fileService.readExcelFile(fileName);
        return false;
    }

    //


}
