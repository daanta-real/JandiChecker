package utils.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;
import ui.UIMain;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public class JTextAppender extends AppenderBase<ILoggingEvent> {

    Logger rootLogger = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    LoggerContext ctx = rootLogger.getLoggerContext();
    PatternLayoutEncoder encoder = new PatternLayoutEncoder();

    // 1. Initializer
    // Make an appender and apply to logger
    public void init() {

        // Encoder
        encoder.setContext(ctx);
        encoder.setPattern("[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{36} ☞ %msg%n");
        encoder.start();

        // Appender
        setContext(ctx);
        start();

        // Apply to log context
        rootLogger.addAppender(this);

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
        String msg = new String(encoder.encode(event));
        JTextArea textArea = UIMain.getTextarea();

        // 2. Make msg string
        textArea.append(msg);

        // 3. Focus the caret to the last line
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);

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
            System.out.printf("  - 자를 구간: 0 ~ %d (끝문자: %d번째)", trimLineEnd, endChar);
            //textarea.replaceRange("", 0, endChar);

        } catch(BadLocationException e) {
            System.out.println("  - 자를 구간을 찾는데 실패했습니다.");
        } catch(Exception e) {
            System.out.println("  - 오래된 로그를 잘라내는 데 실패했습니다.");
        }


    }


}
