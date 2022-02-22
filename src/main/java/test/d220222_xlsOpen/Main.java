package test.d220222_xlsOpen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import $.$;
import main.data.DataSystem;

public class Main {
	public static void main(String[] args) {

		String fileName = "a.xls";

        try {

            FileInputStream file = new FileInputStream(new File(DataSystem.PATH, fileName));

            // Workbook instance 생성 후 첫 번째 시트를 가져옴
            // 특정 시트를 불러올 경우 getSheetAt() 안에 시트명을 넣으면 되고
            // 모든 시트를 순회하고 싶으면
            // 1) 아래 for문을 사용.
            // for(Integer sheetNum : workbook.getNumberOfSheets()) {
            //      XSSFSheet sheet = workbook.getSheetAt(i);
            // }
            // 2) Iterator<Sheet> s = workbook.iterator() 사용
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Iterator를 이요하여 모든 행(row)들을 조회
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // 각각의 행에 존재하는 모든 열(cell)을 순회
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    // cell의 타입이 문자형이냐 숫자형이냐에 따라서 서로 다른 처리 가능.
                    // .getNumericCellValue()는 기본적으로 double형을 리턴함
                    switch (cell.getCellType()) {
                        case NUMERIC:
                            System.out.print((int) cell.getNumericCellValue() + "\t");
                            break;
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                    }
                }
                $.pn();
            }
            file.close();

        } catch (IOException e) { e.printStackTrace(); }

	}
}
