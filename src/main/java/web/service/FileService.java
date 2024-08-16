package web.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import web.model.dao.FileDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.util.*;

@Service
public class FileService{

    @Autowired
    FileDao fileDao;

    // 08.08 이재명 pc주소
    String uploadPath = "C:\\Users\\tj-bu-703-021\\Desktop\\Fashion_manager\\build\\resources\\main\\static\\upload\\";


    // [1] 현재 보이는 표 데이터를 엑셀로 내보내기
    // HTTP 세션에 SQL조회문 임시 저장하고 (조회 화면마다 새로고침 방식) 그 조회문으로 ResultSet을 불러와 엑셀로 내보내기
    // JS 코드:
//    function excelExport(){
//        console.log('excelExport');
//        let xhr = new XMLHttpRequest();
//                xhr.open('GET', '/file/export/excel', true);
//                xhr.responseType = 'blob'; // 서버에서 반환된 데이터를 Blob으로 처리
//                xhr.onload = function() {
//                    if (xhr.status === 200) {
//                        var disposition = xhr.getResponseHeader('Content-Disposition');
//                        var filename = 'downloaded-file';
//                        if (disposition && disposition.indexOf('filename=') !== -1) {
//                            filename = disposition.split('filename=')[1].replace(/"/g, '');
//                        }
//
//                        var link = document.createElement('a');
//                        link.href = window.URL.createObjectURL(xhr.response);
//                        link.download = filename;
//                        document.body.appendChild(link); // 링크를 문서에 추가
//                        link.click(); // 링크 클릭하여 다운로드
//                        document.body.removeChild(link); // 다운로드 후 링크 제거
//
//                        // 메모리 정리를 위한 객체 URL 해제
//                        window.URL.revokeObjectURL(link.href);
//                    } else {
//                        console.error('Download failed: ' + xhr.statusText);
//                    }
//                };
//                xhr.onerror = function() {
//                    console.error('Download failed');
//                };
//                xhr.send();
//    }
    public byte[] exportToExcel(){
        System.out.println("ExcelService");
        try {
            // 엑셀 파일 인터페이스 구현
            Workbook workbook = new XSSFWorkbook();
            // 엑셀 페이지/시트 인터페이스 구현
            Sheet sheet = workbook.createSheet("Data");

            // 현재 세션에 저장된 최근 SQL문으로 조회된 ResultSet
            ResultSet rs = fileDao.exportToExcel();
            System.out.println("rs in service : "+rs );

            // 헤더 작성 (Column/열 레이블/이름들)
            // 0번째 줄 작성 = 테이블 열 명칭들
            Row headerRow = sheet.createRow(0);
            // rs에 저장된 열 개수 정보 (메타데이터에서)
            int columnCount = rs.getMetaData().getColumnCount();


            // 셀 스타일 객체 생성 (첫 줄을 볼드체로 + 배경을 노란색으로)
            CellStyle cellStyle = workbook.createCellStyle();
            // 첫 줄 폰트를 볼드체로 지정할 폰트 객체
            Font font = workbook.createFont();
            font.setBold(true); // 폰트 = 볼드체
            // 셀 스타일 객체에 폰트 서식 등록
            cellStyle.setFont(font);
            // 배경색 노란색으로 설정
            cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


            // 금액 칸 서식 설정
            CellStyle currencyStyle = workbook.createCellStyle();
            // 숫자 형식 설정 (대한민국 원)
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("₩ #,##0"));

            ArrayList<Integer> currencyColumn = new ArrayList<>();

            // 각 셀/칸마다 열 이름으로 지정 (테이블 열 이름들 지정하기)
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = headerRow.createCell(i - 1);
                if (rs.getMetaData().getColumnName(i).contains("금액")){
                    currencyColumn.add(i);
                }
                cell.setCellValue(rs.getMetaData().getColumnName(i));
                cell.setCellStyle(cellStyle);
            }

            // 각각 금액 열의 너비를 6000 픽셀로 설정 (대략 23문자)
            currencyColumn.forEach(col -> {
                sheet.setColumnWidth(col-1, 6000); // columnCount 는 1부터 시작하므로 1 빼주기
            });

            // 데이터 작성
            // 현재 작성하는 행 번호 (0번째 행은 테이블 열 이름들 줄)
            int rowCount = 1;
            // rs.next() 루프
            while (rs.next()) {
                // 행 생성 후 rowCount 1 증가
                Row row = sheet.createRow(rowCount++);
                for (int i = 1; i <= columnCount; i++) {
                    // 각 데이터 저장을 위한 셀 생성
                    Cell cell = row.createCell(i - 1);
                    // rs.getString(숫자) : 해당 숫자 번째 열의 데이터
                    // 먼저 getInt()를 써보고 숫자가 아닌 데이터면 getString()으로 가져오기

                    // 금액 칸 서식 설정 ( 금액 열일때 )
                    if (currencyColumn.contains(i)) {
                        cell.setCellStyle(currencyStyle);
                    }
                    
                    try {
                        cell.setCellValue(rs.getInt(i));
                    } catch (Exception e) {
                        cell.setCellValue(rs.getString(i));
                    }
                }
            }

            // 엑셀 파일을 바이트 배열로 변환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // 엑셀 객체를 바이트 배열로 내보내기
            workbook.write(outputStream);
            // 엑셀 객체 닫기
            workbook.close();
            // 완성된 바이트 배열을 반환
            return outputStream.toByteArray();
        } catch (Exception e) {
            System.out.println("exportToExcel() : " + e);
        }
        return null;
    }

    // ================================ 수업 내용 =================================== //

    // 파일 업로드
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

    // 엑셀 파일 읽기 - 회원, 상품 정보 업로드 등
    public List<Map<String, String>> readExcelFile(String filePath) {
        List<Map<String, String>> sheetList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next(); // 첫 번째 줄 (데이터 레이블)
                List<String> headers = new ArrayList<>(); // headers 리스트 : 테이블 열 레이블
                for (Cell cell : headerRow) {
                    headers.add(cell.toString());
                }
                while (rowIterator.hasNext()) { // 두번쨰 줄부터 (엑셀 데이터)
                    Row row = rowIterator.next();
                    Map<String, String> rowData = new HashMap<>();
                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        rowData.put(headers.get(i), cell.toString()); // key : headers, value : cell
                    }
                    sheetList.add(rowData);
                }
            }
            File file = new File(filePath);
            file.delete();
        } catch (Exception e) {
            System.out.println("ExcelService : " + e);
        }
        return sheetList;
    }

    // HTTP 요청/응답 객체
    @Autowired
    private HttpServletRequest request;  // HTTP 요청 객체, HTTP로 요청이 들어온 정보와 기능 포함
    @Autowired
    private HttpServletResponse response; // HTTP 응답 객체, HTTP로 응답할 때의 정보와 기능 포함

    // [2] 파일 다운로드
    public void fileDownload(String filename) {
        System.out.println("FileService fileDownload");
        System.out.println("filename = " + filename);
        // 1. 다운로드할 경로 설정 (uploadPath)
        // - 업로드된 경로와 다운로드할 파일명 조합
        String downloadPath = uploadPath + filename;
        // - File 클래스는 file 관련된 다양한 메서드를 제공
        // .exists() : 파일 존재여부 true/false
        // .length() : 파일이 있으면 파일 용량을 바이트 개수로 반환 (파일 용량 확인)
        File file = new File(downloadPath);
        // 해당 경로 파일이 존재하면 true, 아니면 false -> false시 return
        if (!file.exists()){return;}
        // 2. 해당 다운로드할 파일을 JAVA로 바이트 형식으로 읽어들이기
        // - 스트림 : JAVA 외부와 통신시 바이트 데이터가 다니는 통로
        // - InputStream : 읽어들이는 통로, OutputStream : 내보내는 통로
        // - Buffered : 버퍼, 특정 위치로 이동하는 동안 잠시 데이터를 보관하는 메모리
        try {
            // ++++++++++++++++++++ 배열을 바이트 배열로 읽어오기 +++++++++++++++++++++
            // 2-1. 파일 입력 스트림 객체 생성
            // BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downloadPath));
            FileInputStream fis = new FileInputStream(downloadPath);
            // 2-2. 파일 용량만큼 배열 선언 (여러개의 바이트가 한 파일)
            long fileSize = file.length();
            byte[] bytes = new byte[(int)fileSize];
            fis.read(bytes); // 경로에 해당하는 파일을 바이트로 가져오기
            fis.close();
            System.out.println(Arrays.toString(bytes));
            // +++++++++++++++++++++++ 바이트 배열을 HTTP 바이트 형식으로 응답하기 +++++++++
            // [3] HTTP 스트림으로 응답하기
            // 3-1 출력 스트림 객체 생성, new BufferedOutputStream (출력할 대상의 스트림 객체)
            //BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(downloadPath));
            ServletOutputStream sos = response.getOutputStream();
            // HTTP 응답의 헤더 속성 추가 : .setHeader(key,value)
            // Content-Disposition : 브라우저가 제공하는 다운로드 형식
            // attachment;filename="다운로드에 표시할 파일명"
            // URLEncoder.encode() : URL 경로상의 한글을 인코딩
            String originalFilename = filename.split("_")[1];
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(originalFilename, "UTF-8"));
            // 3-2 바이트 배열 내보내기/출력/쓰기
            sos.write(bytes);
            // 쓰고 난 후 버퍼 닫기
            sos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // [3] 파일 삭제 (수정에서도 사용)
    public void deleteFile(String oldFileName){
        File file = new File(uploadPath + oldFileName);
        file.delete(); //TODO
    }




}
