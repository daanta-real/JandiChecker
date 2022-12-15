package utils;

import tray.TrayMenues;

import java.awt.*;
import java.io.Console;
import java.util.Calendar;

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

    // Get app to go tray
    public static void trayModeOn() {
        try {

            // 1. Prepare
            // get the SystemTray instance
            // If the system tray isn't supported by the current platform, it throws UnsupportedOperationException
            SystemTray tray = SystemTray.getSystemTray();
            // load an image
            Image image = Toolkit.getDefaultToolkit().getImage("resources/main/img/windows_16x16.ico");

            // 2. popup
            // create a popup menu
            PopupMenu popup = new PopupMenu();
            // add all menues to popup
            popup.add(TrayMenues.getOpenWindowMenuInstance());
            popup.add(TrayMenues.getExitMenuInstance());

            // 3. Icon
            // construct a TrayIcon
            TrayIcon trayIcon = new TrayIcon(image, "JandiChecker", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(TrayMenues.getShowWindowListener());
            trayIcon.addActionListener(TrayMenues.getExitListener());
            // Try to add the tray image. If failed this throws AWTException
            tray.add(trayIcon);

        } catch (UnsupportedOperationException e) { // Current OS does not support tray mode
            // disable tray option in your application or perform other actions
            System.out.println("트레이 기능을 지원하지 않는 기종에서 실행되었습니다.");
        } catch(AWTException e) { // Failed to add the tray image
            System.out.println("트레이 아이콘을 추가하는 데 실패했습니다.");
        } catch(Exception e) { // etc.
            System.out.println("기타 에러가 발생하셨습니다.");
        }

        // ...
        // some time later
        // the application state has changed - update the image
        // if (trayIcon != null) {
        //     trayIcon.setImage(updatedImage);
        // }

    }

}
