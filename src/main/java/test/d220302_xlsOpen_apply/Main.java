package test.d220302_xlsOpen_apply;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import $.$;

public class Main {

	public static void main(String[] args) throws Exception {

		// 시트 데이터 불러오기
		// 현재 경로 따기
		String PATH = Paths.get("").toAbsolutePath().toString();
		$.pn("현재 경로: " + PATH);
		// 파일 객체 부르기
		FileInputStream file = new FileInputStream(new File(PATH, "_test.xlsx"));
		// 파일 객체로부터 시트 객체 뽑아내기
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		// 셋팅 내용 뽑기 준비
		List<String[]> list = new ArrayList<>();
		Iterator<Row> rowIterator = sheet.iterator();
		while(rowIterator.hasNext()) {
			Row row = rowIterator.next();
			$.pn(row.getRowNum() + "행 진입. 이 줄의 마지막 열: " + row.getLastCellNum());
		}
		// 3행으로 이동
		rowIterator.next();
		rowIterator.next();
		rowIterator.next();

		// 각 이름/ID 뽑기
		int i = 0;
		while(rowIterator.hasNext()) {
			// 줄 준비
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			$.pn(row.getRowNum() + "행 진입. 이 줄의 마지막 열: " + row.getLastCellNum());
			// 2열로 이동
			cellIterator.next();
			// 이름 획득
			Cell cell2 = cellIterator.next();
			$.pn(cell2.getAddress() + "(" + cell2.getCellType() + ")");
			String name = cell2.getStringCellValue();
			$.pn(cell2.getStringCellValue());
			// 주소 획득
			Cell cell3 = cellIterator.next();
			$.pn(cell3.getAddress() + "(" + cell3.getCellType() + ")");
			String id = cell3.getStringCellValue();
			String[] rowArray = new String[] {name, id};
		}

		// 완성된 List 출력
		for(String[] s: list) $.pn(Arrays.toString(s));

		// 시트 밑 파일 객체 닫기
		workbook.close();
		file.close();

	}
}
