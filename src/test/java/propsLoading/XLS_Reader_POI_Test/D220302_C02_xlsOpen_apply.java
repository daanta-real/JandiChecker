package propsLoading.XLS_Reader_POI_Test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class D220302_C02_xlsOpen_apply {

	@Test
	public void test() {

		// 시트 데이터 불러오기
		// 현재 경로 따기
		String PATH = Paths.get("").toAbsolutePath().toString();
		log.info("구성원 목록이 담겨 있는 엑셀 파일을 불러옵니다.");
		log.info("현재 경로: " + PATH);

		try(
				// 파일 객체 부르기
				FileInputStream file = new FileInputStream(new File(PATH, "list.xlsx"));
				// 파일 객체로부터 시트 객체 뽑아내기
				XSSFWorkbook workbook = new XSSFWorkbook(file)
				// try가 다 끝나면 위의 file과 workbook 객체에 대해 .close()가 실행되며 파일이 닫아진다.
		) {
			// 최종적으로 sheet 객체를 얻는다.
			XSSFSheet sheet = workbook.getSheetAt(0);
			// sheet 객체를 얻었으면 행과 열의 셀들에 대해 해석에 들어간다.
			// 둘 중 하나 주석을 풀고 테스트해 보시라.
			iteratorOLD(sheet);
			iteratorNEW(sheet);
		} catch(Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}

	}

	// Iterator를 이용한 정밀 조작
	private static void iteratorOLD(Sheet sheet) {

		// 셋팅 내용 뽑기 준비
		List<String[]> list = new ArrayList<>();
		Iterator<Row> rowIterator = sheet.iterator();
		// 3행으로 이동
		rowIterator.next();

		// 각 이름/ID 뽑기
		while(rowIterator.hasNext()) {
			// 줄 준비
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			// 이름 셀로 이동
			Cell cell_name = cellIterator.next();
			// 만약에 빈 셀이 나오면 읽기 종료
			if(cell_name.getCellType() == CellType.BLANK) {
				log.info("이 행에 대해 읽기 종료");
				break;
			}
			String nameString = cell_name.getStringCellValue();
			// Github ID 획득
			Cell cell_id = cellIterator.next();
			String id = cell_id.getStringCellValue();
			// 획득된 구성원 정보를 목록에 추가
			log.info("구성원 목록 추가: " + cell_name.getStringCellValue() + "(" + cell_id.getStringCellValue() + ")");
			String[] rowArray = new String[] {nameString, id};
			list.add(rowArray);
		}

		// 완성된 List 출력
		for(String[] s: list) log.info(Arrays.toString(s));

	}

	// Stream을 이용한 간단 조작
	public static void iteratorNEW(Sheet sheet) {

		// 셋팅 내용 뽑기 준비
		Map<String, String> m = new HashMap<>();

		// 행 루프
		for(Row row: sheet) {
			log.debug("NEW ROW ----------------");

			// 이름 목록 준비
			String studentName = null;
			String gitHubId = null;

			// 열 루프
			for(Cell cell: row) {
				log.debug("Cell: {}", cell);

				// 만약에 빈 셀이 나오면 읽기 종료
				if(cell.getCellType() == CellType.BLANK) {
					log.info("이 행에 대해 읽기 종료");
					break;
				}

				// 셀의 정보를 읽는다.
				int columnIndex = cell.getColumnIndex();
				String value = cell.getStringCellValue();
				if(columnIndex == 0) studentName = value; // 0번째 열은 학생명 칸이다
				else if(columnIndex == 1) gitHubId = value; // 1번 열은 Github ID 칸이다

			}

			// 여기까지 오면 이름과 gitHub ID 둘 다를 얻었을 것.
			// 둘 다 얻은 게 맞다면 Map에 추가한다.
			if(StringUtils.isEmpty(studentName) || StringUtils.isEmpty(gitHubId)) continue;
			m.put(studentName, gitHubId);

		}

		// 결과 출력
		log.debug("결과를 발표함\n{}", m);

	}

}
