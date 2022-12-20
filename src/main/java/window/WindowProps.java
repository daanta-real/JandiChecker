package window;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public final class WindowProps {

    // 1. Fields
    // menues
    public static final MenuItem MENU_SHOW_WINDOW = new MenuItem();
    public static final MenuItem MENU_EXIT = new MenuItem();
    // action listeners
    public static final ActionListener LISTENER_SHOW_WINDOW = e -> runOpen();
    public static final ActionListener LISTENER_EXIT = e -> runExit();

    // 2. Constructor must set private (and class must be final; Because it's a utility class)
    private WindowProps() {
        MENU_SHOW_WINDOW.setShortcut(new MenuShortcut(KeyEvent.VK_O));
        MENU_EXIT.setShortcut(new MenuShortcut(KeyEvent.VK_E));
        MENU_SHOW_WINDOW.setLabel("&Open");
        MENU_EXIT.setLabel("&Exit");
        MENU_SHOW_WINDOW.addActionListener(LISTENER_SHOW_WINDOW);
        MENU_EXIT.addActionListener(LISTENER_EXIT);
    }

    // 3. Methods
    public static void runOpen() {
        System.out.println("요청에 의해 윈도우를 활성화합니다.");
        WindowService.getInstance().activateWindow();
    }

    public static void runExit() {
        System.out.println("요청에 의해 종료합니다.");
        System.exit(0);
    }

}
