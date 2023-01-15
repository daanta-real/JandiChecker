package XLS_Reader_POI_Test;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class D230115_C01_xlsOpenAgain {

    @Test
    public void start() {

        String PATH = Paths.get("").toAbsolutePath().toString();
        log.info("현재 경로: " + PATH);

        try(
            // 파일 객체 부르기
            FileInputStream file = new FileInputStream(new File(PATH, "settings.xlsx"));
            // 파일 객체로부터 시트 객체 뽑아내기
            XSSFWorkbook workbook = new XSSFWorkbook(file)
            // try가 다 끝나면 위의 file과 workbook 객체에 대해 .close()가 실행되며 파일이 닫아진다.
        ) {
            // 최종적으로 sheet 객체를 얻는다.
            XSSFSheet sheet = workbook.getSheetAt(0);

            // master props
            Map<String, Object> props = new HashMap<>();

            // Get Members
            List<Map<String, String>> membersInfo = getMembersInfo(sheet);
            props.put("members", membersInfo);
            log.debug(props.get("members").toString());

        } catch(Exception e) {
            // log.error(ExceptionUtils.getStackTrace(e));
            e.printStackTrace();
        }

    }

    public static List<Map<String, String>> getMembersInfo(XSSFSheet sheet) {

        // Go third row
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        rowIterator.next();

        // Read the member list starting 4th row (address '?4' ~ )
        List<Map<String, String>> membersInfo = new ArrayList<>();
        while(rowIterator.hasNext()) {

            // Target values
            Map<String, String> member = new HashMap<>();
            String name, gitHubID, discordTagID;

            // Get row's cell iterator
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            // Go 2nd column: B cell, member name
            // REQUIRED
            Cell cellName = cellIterator.next(); // 2nd cell (Address 'B?')
            // If meets a blank in B cell, don't read this row anymore and go to next row
            if(cellName.getCellType() == CellType.BLANK) {
                log.info("No member found in ({})", cellName.getAddress());
                continue;
            }
            name = cellName.getStringCellValue();
            member.put("name", name);

            // Go 3rd column: C cell, member's GitHub ID
            // REQUIRED
            Cell cellGitHubID = cellIterator.next(); // 3rd cell (Address 'C?')
            // If meets a blank in C cell, don't read this row anymore and go to next row
            if(cellGitHubID.getCellType() == CellType.BLANK) {
                log.info("Member's name is found but no GitHub ID ({})", cellGitHubID.getAddress());
                continue;
            }
            gitHubID = cellGitHubID.getStringCellValue();
            member.put("gitHubID", gitHubID);

            // Go 4th column: D cell, member's Discord Tag ID
            // OPTIONAL
            Cell cellDiscordTagID = cellIterator.next(); // 4th cell (Address 'D?')
            // If meets a blank in D cell, don't
            if(cellDiscordTagID.getCellType() == CellType.BLANK) {
                log.info("Member's name is found but no Discord Tag ID ({})", cellDiscordTagID.getAddress());
            } else { // Put the value only cell is not blank cause this is optional
                discordTagID = cellDiscordTagID.getStringCellValue();
                member.put("discordTagID", discordTagID);
            }

            membersInfo.add(member);
            log.debug("Member loading compltete: {}", member);

        }

        return membersInfo;

    }
}
