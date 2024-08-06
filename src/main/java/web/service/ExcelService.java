package web.service;

import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {
    public List<Map<String, String>> readExcelFile(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // 첫 번째 시트

            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                Row headerRow = rowIterator.next();
                List<String> headers = new ArrayList<>();

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

                    data.add(rowData);
                }
            }
        } catch (Exception e) {
            System.out.println("ExcelService : " + e);
        }

        return data;
    }
}
