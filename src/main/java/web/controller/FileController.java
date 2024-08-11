package web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.FileService;

@RestController
@RequestMapping("/file")
public class FileController {
    //08.07 첨부파일
    @Autowired
    FileService fileService;

    @GetMapping("/download")
    public void fileDownload(String filename){
        System.out.println("FileController.fileDownload");
        System.out.println("filename = " + filename);
        fileService.fileDownload(filename);
    }

    // [3] 엑셀로 현재 테이블 내보내기
    // 엑셀로 내보내기 버튼 - AJAX 요청 - @Service에서 엑셀 byte[] 받아와 response로 내보내기
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response){
        try {
            // ResultSet을 엑셀 파일로 변환
            byte[] excelBytes = fileService.exportToExcel();

            // HTTP 응답으로 엑셀 파일 반환
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
            response.getOutputStream().write(excelBytes);
        } catch (Exception e) {
            System.out.println("exportToExcel() : " + e);
        }
    }
}
