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

import static init.Pr.pr;

@Slf4j
public class LoaderXLSX {

    private LoaderXLSX() {}

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

            Cell cellDiscordName = CellUtil.getCell(row, 3);
            if(cellDiscordName.getCellType() != CellType.BLANK) {
                String discordName = cellDiscordName.getStringCellValue(); // Discord ID is optional
                memberParam.put("discordName", discordName);
            }

            log.debug("MEMBERS '{}' FOUND: {}", name, memberParam);
            membersInfo.put(name, memberParam);
            count++;

        }

        pr.setMembers(membersInfo);
        log.debug("FINISHED MEMBER LOADING: {}", membersInfo);

    }

    private static void getPropsInfo(XSSFSheet sheet) {
        pr.setLanguage(getStringFromAddr(sheet, 2, 6));
        pr.setCronSchedule(getStringFromAddr(sheet, 5, 6));
        pr.setCronTargetChannelID(getStringFromAddr(sheet, 6, 6));
        pr.setToken_JDA(getStringFromAddr(sheet, 9, 6));
        pr.setToken_ChatGPT(getStringFromAddr(sheet, 10, 6));
        StringBuilder sb = new StringBuilder();
        for(int i = 11; i <= 23; i++) {
            try {
                sb.append(getStringFromAddr(sheet, i, 6));
                if(i != 23) sb.append("\n");
            } catch(Exception e) {
                // Has posibility to come here when failed to read cell value from setting.xls file
            }
        }
        pr.setToken_GoogleCloud(sb.toString());
        log.debug("FINISHED LOADING PROPS!\n\nPROPS:\n{}\n", utils.CommonUtils.getPrettyJSON(pr));
    }

    public static void loadXLSXSettings() throws Exception {
        try(
                // Load the file instance
                FileInputStream file = new FileInputStream(new File(pr.getPath(), "settings.xlsx"));
                // Make the sheet instance from it
                XSSFWorkbook workbook = new XSSFWorkbook(file)
                // After the try and catch statement block JVM automatically load .close() methods of each to close
        ) {
            // Finally get the sheet instance we wanted
            XSSFSheet sheet = workbook.getSheetAt(0);
            log.debug("sheet loading success: {}", sheet);
            getMembersInfo(sheet);
            getPropsInfo(sheet);
        } catch(Exception e) {
            throw new Exception(e);
        }
    }

}
