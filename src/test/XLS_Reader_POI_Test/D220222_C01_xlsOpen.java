package XLS_Reader_POI_Test;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.Iterator;

@Slf4j
public class D220222_C01_xlsOpen {

	@Test
	public void main() throws Exception {

		// 현재 경로 따기
		String PATH = Paths.get("").toAbsolutePath().toString();
		log.info("현재 경로: {}", PATH);

		// 파일 객체 부르기
		FileInputStream file = new FileInputStream(new File(PATH, "_test.xlsx"));
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
				// NUMERIC(0), STRING(1), FORMULA(2), BLANK(3), BOOLEAN(4), ERROR(5)
				// 그 외에 NONE(-1)도 있긴 한데 내부적으로 쓰이는 값이므로 신경쓰지 말자.
				// .getNumericCellValue()로 계산된 셀값을 구할 수 있되, 기본적으로 boolean형만 리턴하니 참고.
				log.info("[" + cell.getAddress() + "(" + cell.getCellType() + ")] => ");
				switch (cell.getCellType()) {

					// 숫자셀인 경우, 기본적으로 숫자 형태로만 얻어진다. 만약 문자형으로 쓰려면 String으로 변환해서 써야 한다.
					// 예제 셀값: 135
					case NUMERIC -> log.info("NUMERIC을 읽었습니다: ", cell.getNumericCellValue());

					// 문자셀인 경우.
					// 예제 셀값: "문자열"
					case STRING -> log.info("STRING 타입입니다: ", cell.getStringCellValue());

					// 에러 셀. 셀 타입 별로 서로 다른 SWITCH를 쓰려면, 에러 셀 case문은 반드시 FORMULA 전에 위치하여야 한다.
					// ERROR도 기본적으로 FORMULA의 일종이기 때문.
					// 예제 셀값: "=(A1:A3/B1:B3)" (에러 남)
					case ERROR -> log.info("ERROR로 인식했습니다: ", cell.getCellFormula());

					// FORMULA(수식)셀인 경우, 결과값을 "문자열"로 빼오거나, 아예 "수식구문"을 빼올 수도 있다.
					// 예제 셀값: "=ADDRESS(ROW(),COLUMN())"
					case FORMULA -> log.info("FORMULA로 인식했습니다: {}, {}, {}", cell.getStringCellValue(), " / ", cell.getCellFormula());

					// 빈 값
					case BLANK -> log.info("BLANK로 인식했습니다: ", cell.getNumericCellValue());

					// TRUE/FALSE 중 하나로 기록했을 경우.
					// 예제 셀값: "TRUE"
					case BOOLEAN -> log.info("BOOLEAN을 읽었습니다: ", cell.getBooleanCellValue());

					// default를 써주지 않으면 switch 판정식에 _NONE 형태에 대한 분기점이 없다고 warning이 발생한다.
					default -> log.info("에러");

				}
				log.info("SWITCH문 끝. 줄 개행");
			}
		}

		// 시트 이용이 끝났다면, 시트를 먼저 닫고 그 다음에 파일을 닫아야 한다.
		// 오류를 내고 싶지 않다면 순서를 지키는 것이 좋다.
		workbook.close();
		file.close();

	}

}
