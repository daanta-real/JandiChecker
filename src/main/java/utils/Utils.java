package utils;

import java.util.Calendar;

import java.io.Console;

public class Utils {

    // Returns a Calendar Instance (Which is set by inputted date info)
    public static Calendar getCalendar(String y, String m, String d) {
        return getCalendar(Integer.parseInt(y), Integer.parseInt(m), Integer.parseInt(d));
    }
    public static Calendar getCalendar(int y, int m, int d) {
        Calendar c = Calendar.getInstance();
        c.set(y, m, d);
        return c;
    }

    // pause in console
    // https://stackoverflow.com/questions/6032118/make-the-console-wait-for-a-user-input-to-close
    public static void waitForEnter() { waitForEnter(""); }
    public static void waitForEnter(String message, Object... args) {
        Console c = System.console();
        if (c != null) {
            // printf-like arguments
            if (message != null) c.format(message, args);
            c.format("\n엔터 키를 누르십시오...\n");
            c.readLine();
        }
    }

}
