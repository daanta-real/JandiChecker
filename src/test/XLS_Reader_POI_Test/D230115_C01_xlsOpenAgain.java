package XLS_Reader_POI_Test;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class D230115_C01_xlsOpenAgain {

    public static Map<String, Object> props = new HashMap<>();

    private static Cell getStringFromAddr(XSSFSheet sheet, int rowIndex, int colIndex) {
        Row row = CellUtil.getRow(rowIndex, sheet);
        return CellUtil.getCell(row, colIndex);
    }

    @Test
    public void init() {

        String PATH = Paths.get("").toAbsolutePath().toString();
        log.info("현재 경로: " + PATH);

        try(
            // 파일 객체 부르기
            FileInputStream file = new FileInputStream(new File(PATH, "settings.xlsx"));
            // 파일 객체로부터 시트 객체 뽑아내기
            XSSFWorkbook workbook = new XSSFWorkbook(file)
            // try가 다 끝나면 위의 file과 workbook 객체에 대해 .close()가 실행되며 파일이 닫아진다.
            // sheet 필드는 이 try 구문이 끝나면 소거되어 버리니, 처리할 것이 있으면 이 안에서 할 것.
        ) {
            // 최종적으로 sheet 객체를 얻는다.
            XSSFSheet sheet = workbook.getSheetAt(0);
            log.debug("sheet loading success: {}", sheet);
//            getMembersInfo_old(sheet);
            getMembersInfo_new(sheet);
            getVersionInfo(sheet);
        } catch(Exception e) {
            e.printStackTrace();
            log.debug("sheet loading failed");
        }

    }

    public void getVersionInfo(XSSFSheet sheet) {
        props.put("version", getStringFromAddr(sheet, 2, 6));
        props.put("build", getStringFromAddr(sheet, 3, 6));
        props.put("cronSchedule", getStringFromAddr(sheet, 6, 6));
        props.put("cronTargetChannelID", getStringFromAddr(sheet, 7, 6));
        props.put("JDAToken", getStringFromAddr(sheet, 10, 6));
        props.put("ChatGPTToken", getStringFromAddr(sheet, 11, 6));
        StringBuilder sb = new StringBuilder();
        for(int i = 12; i <= 23; i++) {
            sb.append(getStringFromAddr(sheet, i, 6));
            if(i != 23) sb.append("\n");
        }
        props.put("GoogleCloudToken", sb.toString());
        log.debug("FINISHED PROPS LOADING! {}", props);
    }

    public static void getMembersInfo_new(XSSFSheet sheet) {

        // Result
        Map<String, Map<String, String>> membersInfo = new HashMap<>();

        int count = 3; // start from 4th row
        while(true) {

            Row row = CellUtil.getRow(count, sheet);

            Cell cellName = CellUtil.getCell(row, 1);
            if(cellName.getCellType() == CellType.BLANK) break; // Name is required, and no more members info from this row
            String name = cellName.getStringCellValue();

            Map<String, String> memberParam = new HashMap<>();

            Cell cellGitHubID = CellUtil.getCell(row, 2);
            if(cellGitHubID.getCellType() == CellType.BLANK) continue; // GitHubID is required
            String gitHubID = cellGitHubID.getStringCellValue();
            memberParam.put("gitHubID", gitHubID);

            Cell cellDiscordTagID = CellUtil.getCell(row, 3);
            if(cellDiscordTagID.getCellType() != CellType.BLANK) {
                String discordTagID = cellDiscordTagID.getStringCellValue(); // Discord ID is optional
                memberParam.put("discordTagID", discordTagID);
            }

            membersInfo.put(name, memberParam);
            count++;

        }

        props.put("members", membersInfo);

    }

    public static void getMembersInfo_old(XSSFSheet sheet) {

        // Result
        List<Map<String, String>> membersInfo = new ArrayList<>();

        // Go to 3rd row
        Iterator<Row> rowIterator = sheet.iterator();
        rowIterator.next();
        rowIterator.next();

        // Read the member list starting 4th row (address '?4' ~ )
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
            // If meets a blank in B cell, don't read members' info anymore and stop reading
            if(cellName.getCellType() == CellType.BLANK) {
                log.info("No member found in ({})", cellName.getAddress());
                log.info("FINISHED MEMBERS LOADING!");
                break;
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

        props.put("members", membersInfo);

    }
}
