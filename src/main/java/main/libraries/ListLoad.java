package main.libraries;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import $.$;

public class ListLoad {

	public static List<String[]> list;

	public static void ready() throws Exception {

		// 시트 데이터 불러오기
		// 현재 경로 따기
		String PATH = Paths.get("").toAbsolutePath().toString();
		$.pn("현재 경로: " + PATH);
		// 파일 객체 부르기
		$.pn("[엑셀 파일로부터 구성원 목록 읽기 시작]");
		FileInputStream file = new FileInputStream(new File(PATH, "list.xlsx"));
		// 파일 객체로부터 시트 객체 뽑아내기
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		// 셋팅 내용 뽑기 준비
		list = new ArrayList<>();
		Iterator<Row> rowIterator = sheet.iterator();
		// 3행으로 이동
		rowIterator.next();

		// 각 이름/ID 뽑기
		int i = 0;
		while(rowIterator.hasNext()) {

			// 읽어들일 칸으로 이동
			// 줄 준비
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			// 이름 셀로 이동
			Cell cell_name = cellIterator.next();
			// 만약에 빈 셀이 나오면 읽기 종료
			if(cell_name.getCellType() == CellType.BLANK) break;

			// 별칭과 아이디를 획득함
			String nameString = cell_name.getStringCellValue();
			// Github ID 획득
			Cell cell_id = cellIterator.next();
			String id = cell_id.getStringCellValue();
			// 획득된 구성원 정보를 목록에 추가
			$.pf("%d번째 구성원 목록 확인: %s (ID: %s)\n", ++i, cell_name.getStringCellValue(), cell_id.getStringCellValue());
			String[] rowArray = new String[] {nameString, id};
			list.add(rowArray);
		}

		// 시트 밑 파일 객체 닫기
		workbook.close();
		file.close();

		$.pf("[엑셀 파일로부터 구성원 목록 읽기 완료(총 인원수: %d명)]\n\n", list.size());

	}

}
