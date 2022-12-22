package utils.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;

public class SwingAppender extends AppenderBase<ILoggingEvent> {

    public SwingAppender() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayout fPatternLayout = new PatternLayout();
        fPatternLayout.setPattern("%d{HH:mm:ss.SSS} - %msg");
        fPatternLayout.setContext(lc);
        fPatternLayout.start();
//        logConsoleThread.setLogTextArea(ui.logTextArea);
//        logConsoleThread.setLayout(fPatternLayout);
//        logConsoleThread.setContext(lc);
//        logConsoleThread.start();
//        Logger logger = lc.getLogger("auction");
//        logger.addAppender(logConsoleThread);
//        logger.setAdditive(false);
//        logger.setLevel(Level.INFO);
    }

    @Override
    public void start() {
//        try {
//            encoder.init(out);
//        } catch (IOException e) {
//        }
//        super.start();
    }
//
    @Override
    public void append(ILoggingEvent event) {
//        try {
//            encoder.doEncode(event);
//            out.flush();
//            String line = out.toString(); // TODO: append _line_ to your JTextPane
//            out.reset();
//        } catch (IOException e) {
//        }
    }
}