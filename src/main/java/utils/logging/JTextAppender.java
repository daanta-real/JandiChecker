package utils.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class JTextAppender extends AppenderBase<ILoggingEvent> {

    // Constructor
    public JTextAppender() {
        super();
        start();
    }

    // Append
    @Override
    protected void append(ILoggingEvent eventObject) {
        System.out.println("meow");
    }

}