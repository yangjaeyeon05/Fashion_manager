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
            System.out.println("excelBytes = " + excelBytes.length);
            // 헤더 생성
            HttpHeaders headers = new HttpHeaders();
            // 파일 이름 정하기
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TableExport.xlsx");
            // 파일 타입 (전송타입)
            headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");

            // byte 배열 resource 객체
            ByteArrayResource resource = new ByteArrayResource(excelBytes);

            // HTTP 응답 객체 생성 및 반환
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
