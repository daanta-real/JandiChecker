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
import main.data.MainSystem;

// 멤버가 적혀 있는 엑셀 파일을 읽어들이는 역할을 하는 클래스
public class XlsReader {

	// 엑셀 경로
	public static final String PATH = Paths.get("").toAbsolutePath().toString(); // 잔디체커가 실행되는 경로
	private static final String XLS_FILE_NAME = "list.xlsx"; // 스터디 목록을 읽어올 엑셀 파일명

	// 명단이 적혀 있는 멤버 목록을 배열로 리턴해줌
	@SuppressWarnings("resource") // workbook이 while문을 돌고 나서 닫히지 않을 수 있다고 warning을 뱉어서 추가함.
	public static String[][] getMembers() {
		$.pf(" - 실행 경로: %s\n", PATH);
		$.pf(" - 엑셀 파일명: %s\n", XLS_FILE_NAME);

		// 시트 데이터 불러오기
		try {

			// 파일 객체 부르기
			$.pn(" - 엑셀 파일 읽기 시작");
			FileInputStream file = new FileInputStream(new File(PATH, XLS_FILE_NAME));

			// 파일 객체로부터 시트 객체 뽑아내기
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			// 셋팅 내용 뽑기 준비
			List<String[]> arrayList_temp = new ArrayList<>();
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
				if(MainSystem.DEBUG) $.pf("   %2d번째 구성원 목록 확인: %s (ID: %s)\n", ++i, cell_name.getStringCellValue(), cell_id.getStringCellValue());
				String[] rowArray = new String[] {nameString, id};
				arrayList_temp.add(rowArray);
			}

			// ArrayList를 String[][]으로 변환
			String[][] members = new String[arrayList_temp.size()][2];
			for(i = 0; i < arrayList_temp.size(); i++) {
				members[i] = arrayList_temp.get(i);
			}

			// 시트 밑 파일 객체 닫기
			workbook.close();
			file.close();

			$.pf(" - 엑셀 파일 읽기 완료. 총 인원수: %d명\n", members.length);
			return members;

		} catch(Exception e) {
			$.pn("에러 발생");
			e.printStackTrace();
			return null;
		}

	}

}
