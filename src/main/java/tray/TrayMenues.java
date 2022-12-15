package tray;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public final class TrayMenues {



    // 1. Fields
    // menues
    private static final MenuItem openWindowMenu = new MenuItem("&Open", new MenuShortcut(KeyEvent.VK_O));
    private static final MenuItem exitMenu = new MenuItem("&Exit", new MenuShortcut(KeyEvent.VK_E));
    // action listeners
    private static final ActionListener exitListener = e -> {
        System.out.println("요청에 의해 종료합니다.");
        System.exit(0);
    };
    private static final ActionListener showWindowListener = e -> {
        System.out.println("요청에 의해 윈도우를 활성화합니다.");
        System.exit(0);
    };



    // 2. Constructor must set private (and class must be final; Because it's a utility class)
    private TrayMenues() {
        // set menu
        openWindowMenu.addActionListener(showWindowListener);
        exitMenu.addActionListener(exitListener);
    }



    // 3. Instance getters
    public static MenuItem getOpenWindowMenuInstance() {
        return openWindowMenu;
    }
    public static MenuItem getExitMenuInstance() {
        return exitMenu;
    }
    public static ActionListener getExitListener() {
        return exitListener;
    }
    public static ActionListener getShowWindowListener() {
        return showWindowListener;
    }



}
