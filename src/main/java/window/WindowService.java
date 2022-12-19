package window;

import configurations.Configurations;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class WindowService extends JFrame {

    // Fields
    private static WindowService instance = null;
    private static boolean windowVisible = true;

    // Constructor
    private WindowService() {
        super("잔디체커 " + Configurations.VERSION + " Build " + Configurations.BUILD);
        instance = new WindowService();
        windowVisible = true;
    }

    // Get app to go tray
    public static void goTray() {
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
            popup.add(WindowProps.MENU_SHOW_WINDOW);
            popup.add(WindowProps.MENU_EXIT);

            // 3. Icon
            // construct a TrayIcon
            TrayIcon trayIcon = new TrayIcon(image, "JandiChecker", popup);
            // set the TrayIcon properties
            trayIcon.addActionListener(WindowProps.LISTENER_SHOW_WINDOW);
            trayIcon.addActionListener(WindowProps.LISTENER_EXIT);
            // Try to add the tray image. If failed this throws AWTException
            tray.add(trayIcon);

            // Hide
            instance.setVisible(false);
            windowVisible = false;

        } catch (UnsupportedOperationException e) { // Current OS does not support tray mode
            // disable tray option in your application or perform other actions
            log.error("트레이 기능을 지원하지 않는 기종에서 실행되었습니다.");
        } catch(AWTException e) { // Failed to add the tray image
            log.error("트레이 아이콘을 추가하는 데 실패했습니다.");
        } catch(Exception e) { // etc.
            log.error("기타 에러가 발생하셨습니다.", e);
        }

        // ...
        // some time later
        // the application state has changed - update the image
        // if (trayIcon != null) {
        //     trayIcon.setImage(updatedImage);
        // }

    }

}
