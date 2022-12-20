package UI;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
public class UIMenu {

    // 1. Fields
    private static final UIMenu INSTANCE = new UIMenu();
    private final SystemTray TRAY = SystemTray.getSystemTray();
    private final PopupMenu POPUP = new PopupMenu();

    // 2. Constructors
    private UIMenu() {

        try {
            
            // Set SystemTray instance
            log.debug("UIMenu.SystemTray 인스턴스 생성 완료");
            MenuItem MENU_SHOW_WINDOW = new MenuItem();
            MenuItem MENU_EXIT = new MenuItem();
            log.debug("UIMenu.MenuItem 인스턴스 생성 완료");
            log.debug("UIMenu.PopupMenu 인스턴스 생성 완료");
    
            // shortcuts
            MENU_SHOW_WINDOW.setShortcut(new MenuShortcut(KeyEvent.VK_O));
            MENU_EXIT.setShortcut(new MenuShortcut(KeyEvent.VK_E));
    
            // labels
            MENU_SHOW_WINDOW.setLabel("&Open");
            MENU_EXIT.setLabel("&Exit");
    
            // listeners
            MENU_SHOW_WINDOW.addActionListener(e -> runOpen());
            MENU_EXIT.addActionListener(e -> runExit());
    
            // add all menues to popup
            POPUP.add(MENU_SHOW_WINDOW);
            POPUP.add(MENU_EXIT);
            
        } catch (Exception e) { // etc.
            log.error("트레이 생성 중 기타 에러가 발생하셨습니다.", e);
            throw e;
        }

        log.info("트레이 생성 완료.");
        
    }

    // 3. Initializer

    // Initialize tray
    public void initTray() throws Exception {

        try {

            // 2. Icon
            // construct a TrayIcon
            Image iconImage = UIMain.getInstance().getImage();
            TrayIcon trayIcon = new TrayIcon(iconImage, "JandiChecker", POPUP);
            // set the TrayIcon double click linstener
            // Try to add the tray image. If failed this throws AWTException
            TRAY.add(trayIcon);

            // ...
            // some time later
            // the application state has changed - update the image
            // if (trayIcon != null) {
            //     trayIcon.setImage(updatedImage);
            // }

        } catch (AWTException e) { // Failed to add the tray image
            log.error("트레이 아이콘을 추가하는 데 실패했습니다.");
            throw e;
        } catch (Exception e) { // etc.
            log.error("트레이 반영 중 기타 에러가 발생하셨습니다.", e);
            throw e;
        }

        log.info("트레이 반영 완료.");

    }

    // Return the singleton instance
    public static UIMenu getInstance() {
        return INSTANCE;
    }

    // 4. Actions

    // Minimization
    public void runGoTray() {
        System.out.println("요청에 의해 윈도우를 트레이로 보냅니다.");
        UIMain.getInstance().setVisible(false);
    }

    public void runOpen() {
        System.out.println("요청에 의해 윈도우를 활성화합니다.");
        UIMain.getInstance().activateWindow();
    }

    public void runExit() {
        System.out.println("요청에 의해 종료합니다.");
        System.exit(0);
    }

}
