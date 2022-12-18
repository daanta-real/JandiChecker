package window;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public final class WindowProps {

    // 1. Fields
    // menues
    public static final MenuItem MENU_SHOW_WINDOW = new MenuItem("&Open", new MenuShortcut(KeyEvent.VK_O));
    public static final MenuItem MENU_EXIT = new MenuItem("&Exit", new MenuShortcut(KeyEvent.VK_E));
    // action listeners
    public static final ActionListener LISTENER_SHOW_WINDOW = e -> {
        System.out.println("요청에 의해 윈도우를 활성화합니다.");
        System.exit(0);
    };
    public static final ActionListener LISTENER_EXIT = e -> {
        System.out.println("요청에 의해 종료합니다.");
        System.exit(0);
    };

    // 2. Constructor must set private (and class must be final; Because it's a utility class)
    private WindowProps() {
        // set menu
        MENU_SHOW_WINDOW.addActionListener(LISTENER_SHOW_WINDOW);
        MENU_EXIT.addActionListener(LISTENER_EXIT);
    }

}
