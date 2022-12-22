package d220302_xlsOpen_apply;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

	public static void main(String[] args) /*throws Exception*/ {
/*
		// 시트 데이터 불러오기
		// 현재 경로 따기
		String PATH = Paths.get("").toAbsolutePath().toString();
		log.info("구성원 목록이 담겨 있는 엑셀 파일을 불러옵니다.");
		log.info("현재 경로: " + PATH);
		// 파일 객체 부르기
		FileInputStream file = new FileInputStream(new File(PATH, "list.xlsx"));
		// 파일 객체로부터 시트 객체 뽑아내기
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		XSSFSheet sheet = workbook.getSheetAt(0);

		// 셋팅 내용 뽑기 준비
		List<String[]> list = new ArrayList<>();
		Iterator<Row> rowIterator = sheet.iterator();
		// 3행으로 이동
		rowIterator.next();

		// 각 이름/ID 뽑기
		int i = 0;
		while(rowIterator.hasNext()) {
			// 줄 준비
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			// 이름 셀로 이동
			Cell cell_name = cellIterator.next();
			// 만약에 빈 셀이 나오면 읽기 종료
			if(cell_name.getCellType() == CellType.BLANK) {
				log.info("읽기 종료");
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

		// 시트 밑 파일 객체 닫기
		workbook.close();
		file.close();
*/
	}
}
