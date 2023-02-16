package ui;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import static init.Pr.pr;

@Slf4j
public final class UIMain extends JFrame {

    // 1. Fields
    private static UIMain uiMain;
    private static final JTextArea TEXTAREA = new JTextArea();


    private final JPanel WINDOW;
    private final Image IMAGE;
    private final Font FONT;


    // 2. Constructor
    private UIMain() throws Exception {

        log.info("WindowService - {} - {}",
                pr.l("initialization"),
                pr.l("start"));

        // Set JPanel(main window) instance
        WINDOW = new JPanel();
        log.info("WindowService - {} - creation - {}",
                pr.l("instance"),
                pr.l("fin"));

        // Set an image icon
        URL imgUrl;
        try {
            imgUrl = getClass().getClassLoader().getResource("windows_16x16.png");
        } catch(Exception e) {
            log.debug("{} - {} - {}",
                    pr.l("icon"),
                    pr.l("initialization"),
                    pr.l("err_occurred"));
            throw new Exception();
        }
        IMAGE = Toolkit.getDefaultToolkit().getImage(imgUrl);
        log.info("{} - {} - {}",
                pr.l("icon"),
                pr.l("initialization"),
                pr.l("fin"));

        // Set font
        InputStream fontStream;
        try {
            fontStream = Objects.requireNonNull(getClass().getResourceAsStream("/font.otf"));
        } catch(Exception e) {
            log.debug("{} - {} - {}",
                    pr.l("font"),
                    pr.l("initialization"),
                    pr.l("err_occurred"));
            throw new Exception();
        }
        FONT = Font
                .createFont(Font.TRUETYPE_FONT, fontStream)
                .deriveFont(16f); // Make sure to derive the size;
        UIManager.getLookAndFeelDefaults().put("defaultFont", FONT);
        log.debug("{} - {} - {}",
                pr.l("font"),
                pr.l("initialization"),
                pr.l("fin"));

    }

    // 3. Initializer
    public static void init() {

        try {
            uiMain = new UIMain();
        } catch (Exception e) {
            log.info("UIMain - {} - {}",
                    pr.l("instance"),
                    pr.l("error_occurred"));
            throw new RuntimeException(e);
        }

        try {

            // Font
            uiMain.setFont(uiMain.FONT);
            uiMain.add(uiMain.WINDOW);

            // Icon
            uiMain.setIconImage(uiMain.IMAGE);

            // Background Color
            uiMain.setBackground(Color.DARK_GRAY); // This Content area

            // Layout
            uiMain.setLayout(new BorderLayout());
            uiMain.setSize(1400, 800);

            // Top box (= text box)
            TEXTAREA.setEditable(false);
            TEXTAREA.setForeground(new Color(169, 183, 198));
            TEXTAREA.setBackground(new Color(43, 43, 43));
            TEXTAREA.setFont(uiMain.FONT);
            TEXTAREA.setLineWrap(true);
            uiMain.add(TEXTAREA, BorderLayout.CENTER);

            // Scrollbar
            JScrollPane scroll = new JScrollPane(
                    TEXTAREA,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            );
            uiMain.add(scroll);

            // Show
            uiMain.setVisible(true);

            // Reserve exit button in main window
            uiMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Initialize ui Menues
            UIMenu ui = UIMenu.getInstance();
            ui.initTray();

            // Reserve making tray action for clicking minimize button
            uiMain.addWindowStateListener(e -> {
                if ((e.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED){
                    uiMain.runGoTray();
                }
            });

        } catch (Exception e) {
            log.info("Window - {} - {}",
                    pr.l("initialization"),
                    pr.l("err_occurred"));
            throw e;
        }

    }

    // 4. Getter
    public static UIMain getUiMain() {
        return uiMain;
    }
    public Image getImage() {
        return IMAGE;
    }
    public static JTextArea getTextarea() { return TEXTAREA; }
    // 4. Actions

    // Minimization
    public void runGoTray() {
        log.info("{}: {} → {}",
                pr.l("request"),
                pr.l("window"),
                pr.l("tray"));
        setVisible(false);
        UIMenu.trayIconOn();
    }

    public void runGoActivate() {
        log.info("{}: {} → {}",
                pr.l("request"),
                pr.l("window"),
                pr.l("activation"));
        UIMenu.trayIconOff();
        setVisible(true);
        toFront();
        setState(Frame.NORMAL);
    }

    public void runExit() {
        log.info("{}: {}",
                pr.l("request"),
                pr.l("exit"));
        System.exit(0);
    }

}
