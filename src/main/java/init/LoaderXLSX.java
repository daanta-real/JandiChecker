package init;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import static init.Initializer.props;

@Slf4j
public class LoaderXLSX {

    private static String getStringFromAddr(XSSFSheet sheet, int rowIndex, int colIndex) {
        Row row = CellUtil.getRow(rowIndex, sheet);
        return CellUtil.getCell(row, colIndex).getStringCellValue();
    }

    private static void getMembersInfo(XSSFSheet sheet) {

        // Result
        Map<String, Map<String, String>> membersInfo = new HashMap<>();

        int count = 3; // start from 4th row
        while(true) {

            Row row = CellUtil.getRow(count, sheet);

            Cell cellName = CellUtil.getCell(row, 1);
            if(cellName.getCellType() == CellType.BLANK) break; // Name is required, and no more members info from this row
            String name = cellName.getStringCellValue();

            Map<String, String> memberParam = new HashMap<>();
            memberParam.put("name", name);

            Cell cellGitHubID = CellUtil.getCell(row, 2);
            if(cellGitHubID.getCellType() == CellType.BLANK) continue; // GitHubID is required
            String gitHubID = cellGitHubID.getStringCellValue();
            memberParam.put("gitHubID", gitHubID);

            Cell cellDiscordTagID = CellUtil.getCell(row, 3);
            if(cellDiscordTagID.getCellType() != CellType.BLANK) {
                String discordTagID = cellDiscordTagID.getStringCellValue(); // Discord ID is optional
                memberParam.put("discordTagID", discordTagID);
            }

            log.debug("MEMBERS '{}' FOUND: {}", name, memberParam);
            membersInfo.put(name, memberParam);
            count++;

        }

        props.setMembers(membersInfo);
        log.debug("FINISHED MEMBER LOADING: {}", membersInfo);

    }

    private static void getPropsInfo(XSSFSheet sheet) {
        props.setVersion(getStringFromAddr(sheet, 2, 6));
        props.setBuild(getStringFromAddr(sheet, 3, 6));
        props.setLanguage(getStringFromAddr(sheet, 6, 6));
        props.setCronSchedule(getStringFromAddr(sheet, 9, 6));
        props.setCronTargetChannelID(getStringFromAddr(sheet, 10, 6));
        props.setToken_JDA(getStringFromAddr(sheet, 13, 6));
        props.setToken_ChatGPT(getStringFromAddr(sheet, 14, 6));
        StringBuilder sb = new StringBuilder();
        for(int i = 15; i <= 26; i++) {
            sb.append(getStringFromAddr(sheet, i, 6));
            if(i != 26) sb.append("\n");
        }
        props.setToken_GoogleCloud(sb.toString());
        log.debug("FINISHED PROPS LOADING! {}", props);
    }

    public static void loadXLSXSettings(Props props) throws Exception {
        try(
                // 파일 객체 부르기
                FileInputStream file = new FileInputStream(new File(props.getPath(), "settings.xlsx"));
                // 파일 객체로부터 시트 객체 뽑아내기
                XSSFWorkbook workbook = new XSSFWorkbook(file)
                // try가 다 끝나면 위의 file과 workbook 객체에 대해 .close()가 실행되며 파일이 닫아진다.
                // sheet 필드는 이 try 구문이 끝나면 소거되어 버리니, 처리할 것이 있으면 이 안에서 할 것.
        ) {
            // 최종적으로 sheet 객체를 얻는다.
            XSSFSheet sheet = workbook.getSheetAt(0);
            log.debug("sheet loading success: {}", sheet);
            getMembersInfo(sheet);
            getPropsInfo(sheet);
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

}
