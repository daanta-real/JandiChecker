package ui;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static init.Pr.pr;

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
            log.info("UIMenu.SystemTray {} - {} - {}",
                    pr.l("instance"),
                    pr.l("initialization"),
                    pr.l("fin"));
            MenuItem MENU_SHOW_WINDOW = new MenuItem();
            MenuItem MENU_EXIT = new MenuItem();
            log.info("UIMenu.MenuItem {} - {} - {}",
                    pr.l("instance"),
                    pr.l("initialization"),
                    pr.l("fin"));
            log.info("UIMenu.PopupMenu {} - {} - {}",
                    pr.l("instance"), pr.l("initialization"),
                    pr.l("fin"));
    
            // shortcuts
            MENU_SHOW_WINDOW.setShortcut(new MenuShortcut(KeyEvent.VK_O));
            MENU_EXIT.setShortcut(new MenuShortcut(KeyEvent.VK_E));
    
            // labels
            MENU_SHOW_WINDOW.setLabel("Open");
            MENU_EXIT.setLabel("Exit");
    
            // listeners
            MENU_SHOW_WINDOW.addActionListener(e -> UIMain.getUiMain().runGoActivate());
            MENU_EXIT.addActionListener(e -> UIMain.getUiMain().runExit());
    
            // add all menues to popup
            POPUP.add(MENU_SHOW_WINDOW);
            POPUP.add(MENU_EXIT);
            
        } catch (Exception e) { // etc.
            log.debug("{} - {} - {} - {}",
                    pr.l("tray"),
                    pr.l("icon"),
                    pr.l("initialization"),
                    pr.l("err_occurred"));
            throw e;
        }

        log.debug("{} - {} - {}",
                pr.l("tray"),
                pr.l("initialization"),
                pr.l("fin"));
        
    }

    // 3. Initializer

    // Initialize tray
    public void initTray() {

        UIMain uiMain = UIMain.getUiMain();

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
                        log.info("DOUBLE CLICKED THE TRAY ICON.");
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
            log.error("UNKNOWN ERROR HAS OCCURRED", e);
            throw e;
        }

        log.info("FINISHED TRAY INITIALIZATION.");

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
            log.debug("{} - {} - {} - {}",
                    pr.l("tray"),
                    pr.l("icon"),
                    pr.l("initialization"),
                    pr.l("err_occurred"));
        }
    }

    // Hide tray icon
    public static void trayIconOff() {
        UIMenu ui = UIMenu.getInstance();
        ui.TRAY.remove(ui.ICON);
    }

}
