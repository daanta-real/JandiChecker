package test.d220222_xlsOpen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import $.$;

public class Main {
	public static void main(String[] args) {

		try {

			// 잔디체커의 시작
			$.pn("[잔디체커를 실행합니다.]");

			// 현재 경로 따기
			String PATH = Paths.get("").toAbsolutePath().toString();
			System.out.println("현재 경로: " + PATH);

			// 파일 객체 부르기
			FileInputStream file = new FileInputStream(new File(PATH, "★settings.xlsx"));

			// 파일 객체로부터 시트 객체 생성하기
			// Workbook instance 생성 후
			// 1. .getSheetAt(시트 순번 혹은 시트명) 을 통해 시트를 객체로 가져올 수 있다.
			// 2. 만약 상기 시트 객체를 순회하려면
			//    1) 아래 for문을 사용.
			//       for(Integer sheetNum : workbook.getNumberOfSheets()) {
			//           XSSFSheet sheet = workbook.getSheetAt(i);
			//       }
			//    2) Iterator<Sheet> s = workbook.iterator();
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);

			// 아래는 한 시트의 모든 행(r)과 열(c)을 순회하는 예시이다.
			// Iterator를 이요하여 모든 행(row)들을 조회. 여기서 Row 클래스는 POI에서 제공하는 각 행 객체임.
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				// 각각의 행에 존재하는 모든 열(cell)을 순회하려면 row.cellIterator()를 사용해야 한다.
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					// cell의 타입이 문자형이냐 숫자형이냐에 따라서 서로 다른 처리 가능.
					// POI에서 제공하는 셀 타입 상수는 총 여섯 가지이다.
					//     NUMERIC(0), STRING(1), FORMULA(2),
					//     BLANK(3), BOOLEAN(4), ERROR(5)
					// 그 외에 NONE(-1)도 있긴 한데 내부적으로 쓰이는 값이므로 신경쓰지 말자.
					// .getNumericCellValue()는 기본적으로 double형을 리턴함을 참고.
					switch (cell.getCellType()) {
						case NUMERIC:
							System.out.print((int) cell.getNumericCellValue() + "\t");
							break;
						case STRING:
							System.out.print(cell.getStringCellValue() + "\t");
							break;
						default:
							System.out.println("타입이 없습니다.");
							break;
					}
				}
				$.pn("줄 개행");
			}

			// 시트 이용이 끝났다면, 시트를 먼저 닫고 그 다음에 파일을 닫아야 한다.
			// 오류를 내고 싶지 않다면 순서를 지키는 것이 좋다.
			workbook.close();
			file.close();

		} catch (IOException e) { e.printStackTrace(); }

	}
}
