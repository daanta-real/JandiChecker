package utils.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;
import ui.UIMain;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.util.Optional;

public class JTextAppender extends AppenderBase<ILoggingEvent> {

    // 1. Initializer
    // Make an appender and apply to logger
    public void init() {
        LoggerContext ctx = (LoggerContext) LoggerFactory.getILoggerFactory();
        setContext(ctx);
        start();
        ((ch.qos.logback.classic.Logger)LoggerFactory.getLogger("JTextAppender")).addAppender(this);
    }

    // 2. Append action
    @Override
    protected void append(ILoggingEvent event) {
        appendNewMsg(event);
        trimOldMsgOverflow();
    }


    // 3. Append received msg to LOGBOX
    private void appendNewMsg(ILoggingEvent event) {

        // 1. Prepare variables
        String msgOrg = event.getMessage();
        JTextArea textarea = UIMain.getTextarea();

        // 2. Make msg string
        String msg = Optional.ofNullable(msgOrg).orElse("") + "\n";
        System.out.printf("  - nullable 검사 후 메세지: %s\n", msg);
        textarea.append(msg);
        System.out.printf("  - textarea 에 메세지를 추가했습니다: %s\n", textarea);
        
    }

    // 4. Trim old messages
    private void trimOldMsgOverflow() {

        try {


            // 1. Variables
            JTextArea textarea = UIMain.getTextarea();
            int maxLines = 40;
            int totalLines = textarea.getLineCount();
            System.out.printf("  - 라인 수: %d/%d\n", totalLines, maxLines);

            // 2. Trim
            if (totalLines <= maxLines) {
                System.out.println("  - 라인을 자를 필요가 없습니다.");
                return;
            }
            int trimLineEnd = totalLines - maxLines;
            int endChar = textarea.getLineEndOffset(trimLineEnd);
            System.out.printf("  - 자를 구간: 0 ~ %d (끝문자: %d번째)\n", trimLineEnd, endChar);
            //textarea.replaceRange("", 0, endChar);

        } catch(BadLocationException e) {
            System.out.println("  - 자를 구간을 찾는데 실패했습니다.");
        } catch(Exception e) {
            System.out.println("  - 오래된 로그를 잘라내는 데 실패했습니다.");
        }


    }


}
