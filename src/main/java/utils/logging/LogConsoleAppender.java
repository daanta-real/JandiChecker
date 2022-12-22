package utils.logging;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.apache.commons.lang3.StringUtils;
import ui.UIMain;

import javax.swing.*;
import java.util.Optional;

public class LogConsoleAppender extends AppenderBase<ILoggingEvent> {

    // 1.
    JTextPane LOGBOX = UIMain.getLogbox();
    PatternLayout layout;

    // 2. Methods
    @Override
    protected void append(final ILoggingEvent event) {
        SwingUtilities.invokeLater(() -> {
            String text = Optional.ofNullable(LOGBOX.getText()).orElse("");
            text = StringUtils.substring(text, -50000);
            if (text.length() > 100000) text = text.substring(50000);
//            temp += "\n" + layout.doLayout(event);
//            logTextArea.setText(temp);
//            if (temp.length() > 0) {
//                int pos = temp.lastIndexOf("\n") + 1;
//                logTextArea.setCaretPosition(pos);
//            }
        });
    }
}