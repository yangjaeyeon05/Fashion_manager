package web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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


    // [1] 엑셀로 현재 테이블 내보내기
    // 엑셀로 내보내기 버튼 - AJAX 요청 - @Service에서 엑셀 byte[] 받아와 response로 내보내기
    @GetMapping("/export/excel")
    public ResponseEntity<ByteArrayResource> exportToExcel(HttpServletResponse response){
        try {
            System.out.println("excelController");
            // @Service에서 ResultSet을 바이트배열로 변환해서 받아온 바이트배열
            byte[] excelBytes = fileService.exportToExcel();
            System.out.println("bytearray : " + excelBytes.length);
//            // HTTP 응답으로 엑셀 파일 반환
//                // 파일 타입 : .xlsx 파일
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//                // 파일 이름 : TableExport.xlsx
//            response.setHeader("Content-Disposition", "attachment; filename=TableExport.xlsx");
//                // 엑셀 파일 전송
//            response.getOutputStream().write(excelBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TableExport.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            ByteArrayResource resource = new ByteArrayResource(excelBytes);

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(excelBytes.length)
                    .body(resource);
        } catch (Exception e) {
            System.out.println("exportToExcel() : " + e);
        }
        return null;
    }

    // ====================================== 수업 내용 ============================= //

    @GetMapping("/download")
    public void fileDownload(String filename){
        System.out.println("FileController.fileDownload");
        System.out.println("filename = " + filename);
        fileService.fileDownload(filename);
    }

}
