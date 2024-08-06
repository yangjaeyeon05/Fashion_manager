package web.service;

import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ExcelService {

    // 저장할 경로 만들기 (전역변수)
    String uploadPath = "C:\\Users\\tj-bu-703-06\\Desktop\\TJ_2024A_Spring\\src\\main\\resources\\static\\upload\\";

    // [1] 파일 업로드
    // 매개변수로 파일의 바이트가 저장된 MultipartFile 인터페이스
    // 업로드된 파일명 반환
    public String fileUpload(MultipartFile multipartFile){
        System.out.println(multipartFile.getContentType());
        System.out.println(multipartFile.isEmpty());
        System.out.println(multipartFile.getSize());
        // 1. 파일 실제 이름 추출 -> 파일명 생성
        // * 클라이언트(유저)들이 서로 다른 파일을 같은 이름으로 업로드하면 식별이 불가능
        // 해결방안 : 1. UUID() 2.조합 식별 설계 (주로 업로드 날짜/시간 + 파일명)
        String uuid = UUID.randomUUID().toString(); // UUID 난수 생성, UUID 규약에 따른 임의의 문자열
        System.out.println("uuid = " + uuid);
        String fileName = multipartFile.getOriginalFilename();
        // UUID + 파일명 합치기, UUID와 파일명 사이에 구분 문자 조합, 파일명에 구분문자가 존재하면 안된다("_" 등
        // 후에 구분 문자 기준으로 분해하면 [0]UUID, [1]순수파일명
        // .replaceAll(기존문자,새문자)
        fileName = uuid +"_"+ fileName.replaceAll("_", "-"); // 파일명에 _가 존재하면 -로 변경 -> _를 구분문자로 쓰기 위해
        System.out.println("fileName = " + fileName);

        // 3. 저장할 경로와 파일명 합치기
        String filePath = uploadPath + fileName;
        // 4. 해당 경로로 설정한 file 객체, transferTo(file객체)
        File file = new File(filePath);
        // 5. transferTo(file객체) : file객체내 설정한 해당 경로로 파일 복사/저장/이동
        // 일반예외 예외처리 필요
        System.out.println("file = " + file);
        try {
            multipartFile.transferTo(file);
            return fileName;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public List<Map<String, String>> readExcelFile(String filePath) {
        List<Map<String, String>> sheetList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next(); // 첫 번째 줄
                List<String> headers = new ArrayList<>(); // 테이블 열 레이블
                for (Cell cell : headerRow) {
                    headers.add(cell.toString());
                }
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Map<String, String> rowData = new HashMap<>();

                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        rowData.put(headers.get(i), cell.toString());
                    }

                    sheetList.add(rowData);
                }
            }
        } catch (Exception e) {
            System.out.println("ExcelService : " + e);
        }
        return sheetList;
    }
}
