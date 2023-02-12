package logging;

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

    // 1. Fields
    private final Logger rootLogger;
    private final LoggerContext ctx;
    private final PatternLayoutEncoder encoder;

    // 2. Constructor
    private JTextAppender() {
        rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        ctx = rootLogger.getLoggerContext();
        encoder = new PatternLayoutEncoder();
    }

    // 3. Initializer
    // Make an appender and apply to logger
    public static void init() {

        // 1. Making instance
        JTextAppender instance = new JTextAppender();

        // 2. Encoder
        // Original Pattern: "[%d{yyyy-MM-dd HH:mm:ss}:%-3relative][%thread] %-5level %logger{36} ☞ %msg%n"
        instance.encoder.setContext(instance.ctx);
        instance.encoder.setPattern("[%d{yyyy-MM-dd HH:mm:ss}][%thread] ☞ %msg%n");
        instance.encoder.start();

        // 3. Appender
        instance.setContext(instance.ctx);
        instance.start();

        // 4. Apply to log context
        instance.rootLogger.addAppender(instance);

    }

    // 4. Append - main
    @Override
    protected void append(ILoggingEvent event) {
        appendNewMsg(event);
        trimOldMsgOverflow();
    }

    // 5. Append 1 - add new msg to console
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

    // 6. Append 2 - Trim old messages
    private void trimOldMsgOverflow() {

        try {

            // 1. Variables
            JTextArea textarea = UIMain.getTextarea();
            int maxLines = 2000;
            int totalLines = textarea.getLineCount();
            // System.out.printf("  - line counts: %d/%d\n", totalLines, maxLines);

            // 2. Trim
            if (totalLines <= maxLines) {
                // System.out.println("  - not needed to trim the line");
                return;
            }
            int trimLineEnd = totalLines - maxLines;
            int lastChar = textarea.getLineEndOffset(trimLineEnd);
            // System.out.printf("  - trimming section: 0 ~ %d (last char: %dth)", trimLineEnd, lastChar);
            textarea.replaceRange("", 0, lastChar);

        } catch(BadLocationException e) {
            // System.out.println("  - failed to find trimming section.");
        } catch(Exception e) {
            // System.out.println("  - failed to trim the old logs.");
        }

    }

}
