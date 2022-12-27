package ui;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
public class UIMenu {

    // 1. Fields
    private static final UIMenu INSTANCE = new UIMenu();
    private final SystemTray TRAY = SystemTray.getSystemTray();
    private TrayIcon ICON;
    private final PopupMenu POPUP = new PopupMenu();

    // 2. Constructor
    private UIMenu() {

        try {
            
            // Set SystemTray instance
            log.info("UIMenu.SystemTray 인스턴스 생성 완료");
            MenuItem MENU_SHOW_WINDOW = new MenuItem();
            MenuItem MENU_EXIT = new MenuItem();
            log.info("UIMenu.MenuItem 인스턴스 생성 완료");
            log.info("UIMenu.PopupMenu 인스턴스 생성 완료");
    
            // shortcuts
            MENU_SHOW_WINDOW.setShortcut(new MenuShortcut(KeyEvent.VK_O));
            MENU_EXIT.setShortcut(new MenuShortcut(KeyEvent.VK_E));
    
            // labels
            MENU_SHOW_WINDOW.setLabel("Open");
            MENU_EXIT.setLabel("Exit");
    
            // listeners
            MENU_SHOW_WINDOW.addActionListener(e -> UIMain.getInstance().runGoActivate());
            MENU_EXIT.addActionListener(e -> UIMain.getInstance().runExit());
    
            // add all menues to popup
            POPUP.add(MENU_SHOW_WINDOW);
            POPUP.add(MENU_EXIT);
            
        } catch (Exception e) { // etc.
            log.info("트레이 생성 중 기타 에러가 발생하셨습니다.", e);
            throw e;
        }

        log.info("트레이 생성 완료.");
        
    }

    // 3. Initializer

    // Initialize tray
    public void initTray() {

        UIMain uiMain = UIMain.getInstance();

        try {

            // 2. Icon
            // construct a TrayIcon
            Image iconImage = uiMain.getImage();
            ICON = new TrayIcon(iconImage, "JandiChecker", POPUP);

            // Reserve activating windows action for double-clicking tray icon
            ICON.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                        log.info("트레이 아이콘이 더블 클릭되었습니다.");
                        uiMain.runGoActivate();
                    }
                }
            });

            // ...
            // some time later
            // the application state has changed - update the image
            // if (trayIcon != null) {
            //     trayIcon.setImage(updatedImage);
            // }

        } catch (Exception e) { // etc.
            log.error("트레이 초기화 중 기타 에러가 발생하셨습니다.", e);
            throw e;
        }

        log.info("트레이 초기화 완료.");

    }

    // 4. Getter

    // Return the singleton instance
    public static UIMenu getInstance() {
        return INSTANCE;
    }

    // 5. Actions

    // Show tray icon
    public static void trayIconOn() {
        try {
            UIMenu ui = UIMenu.getInstance();
            ui.TRAY.add(ui.ICON);
        } catch(AWTException e) {
            log.error("트레이 아이콘 생성에 실패하였습니다.");
        }
    }

    // Hide tray icon
    public static void trayIconOff() {
        UIMenu ui = UIMenu.getInstance();
        ui.TRAY.remove(ui.ICON);
    }

}
