package utils.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import org.slf4j.LoggerFactory;

public class JTextAppender extends AppenderBase<ILoggingEvent> {

    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
    PatternLayout layout = new PatternLayout();

    // Constructor
    public JTextAppender() {

        super();

        layout.setPattern("meow %msg");
        layout.setContext(lc);
        layout.start();

        start();

    }

    // Append
    @Override
    protected void append(final ILoggingEvent event) {

        System.out.println("meow");
        layout.doLayout(event);

    }

}