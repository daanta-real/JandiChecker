//package Logging;
//
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.core.AppenderBase;
//import ch.qos.logback.core.encoder.EchoEncoder;
//import ch.qos.logback.core.encoder.Encoder;
//import org.slf4j.LoggerFactory;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//public class ConsoleAppender extends AppenderBase<ILoggingEvent> {
//
//    private final Encoder<ILoggingEvent> encoder = new EchoEncoder<>();
//    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//    public ConsoleAppender() {
//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        setContext(lc);
//        start();
//        lc.getLogger("ROOT").addAppender(this);
//    }
//
//    @Override
//    public void start() {
//        try {
//            encoder.init(out);
//        } catch (IOException e) {
//        }
//        super.start();
//    }
//
//    @Override
//    public void append(ILoggingEvent event) {
//        try {
//            encoder.doEncode(event);
//            out.flush();
//            String line = out.toString(); // TODO: append _line_ to your JTextPane
//            out.reset();
//        } catch (IOException e) {
//        }
//    }
//}