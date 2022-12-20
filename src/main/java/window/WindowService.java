package window;

import configurations.Configurations;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@Slf4j
public final class WindowService extends JFrame {

    // 1. Fields
    private static final WindowService INSTANCE;

    static {
        try {
            INSTANCE = new WindowService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final JPanel WINDOW;
    private final SystemTray TRAY;
    private final PopupMenu POPUP_MENU;

    private final Image IMAGE;
    private final Font FONT;


    // 2. Constructor
    private WindowService() throws Exception {

        log.debug("WindowService 초기화 시작");

        // Set JPanel(main window) instance
        WINDOW = new JPanel();
        log.debug("WindowService 인스턴스 생성 완료");

        // Set SystemTray instance
        // if the system tray isn't supported by the current platform, it throws UnsupportedOperationException
        TRAY = SystemTray.getSystemTray();
        log.debug("SystemTray 인스턴스 생성 완료");

        // Set PopupMenu instance
        POPUP_MENU = new PopupMenu();
        log.debug("PopupMenu 인스턴스 생성 완료");

        // Set an image icon
        URL imgUrl = getClass().getClassLoader().getResource("img/windows_16x16.png");
        IMAGE = Toolkit.getDefaultToolkit().getImage(imgUrl);
        log.debug("잔디체커 이미지 로드 완료");

        // Set font
        InputStream fontStream = Objects.requireNonNull(getClass().getResourceAsStream("/font.ttf"));
        FONT = Font
                .createFont(Font.TRUETYPE_FONT, fontStream)
                .deriveFont(15f); // Make sure to derive the size;
        log.debug("폰트 로딩 완료");

        UIManager.getLookAndFeelDefaults()
                .put("defaultFont", FONT);

    }

    // 3. Getter
    public static WindowService getInstance() {
        return INSTANCE;
    }

    // 4. Methods
    // Initialize window
    private void initWindow() {

        try {

            // Title
            setTitle("잔디체커 " + Configurations.VERSION + " Build " + Configurations.BUILD);
            setFont(FONT);
            add(WINDOW);

            // Icon
            setIconImage(IMAGE);

            // Background Color
            setBackground(Color.DARK_GRAY); // This Content area

            // Layout
            setLayout(new BorderLayout());
            setSize(1200, 500);

            // Top box (= text box)
            JTextPane topTextBox = new JTextPane();
            topTextBox.setContentType("text/plain");
            topTextBox.setEditable(false);
            topTextBox.setBackground(new Color(160, 160, 160));
            topTextBox.setText("오우예");
            topTextBox.setFont(FONT);
            add(topTextBox, BorderLayout.CENTER);

            // Bottom Box
            JPanel bottomBox = new JPanel();
            bottomBox.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
            bottomBox.setBackground(Color.GRAY);
            add(bottomBox, BorderLayout.PAGE_END);

            // Button
            JButton button_minimize = new JButton();
            button_minimize.setText("최소화");
            button_minimize.setBackground(new Color(208, 208, 208));
            button_minimize.setFont(FONT);
            bottomBox.add(button_minimize);

            // Button
            JButton button_exit = new JButton();
            button_exit.setText("종료");
            button_exit.setBackground(new Color(80, 80, 80));
            button_exit.setForeground(Color.LIGHT_GRAY);
            button_exit.setFont(FONT);
            bottomBox.add(button_exit);

            // Show
            setVisible(true);

            // Reserve exit button in main window
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Reserve minimize button in main window
            addWindowStateListener(e -> goTray());

        } catch (Exception e) {
            log.error("창 초기화 중 에러가 발생하였습니다.");
            throw e;
        }

    }

    // Initialize tray
    private void initTray() {

        try {

            // 1. popup
            // add all menues to popup
            POPUP_MENU.add(WindowProps.MENU_SHOW_WINDOW);
            POPUP_MENU.add(WindowProps.MENU_EXIT);

            // 2. Icon
            // construct a TrayIcon
            TrayIcon trayIcon = new TrayIcon(IMAGE, "JandiChecker", POPUP_MENU);
            // set the TrayIcon double click linstener
            // Try to add the tray image. If failed this throws AWTException
            TRAY.add(trayIcon);

            // ...
            // some time later
            // the application state has changed - update the image
            // if (trayIcon != null) {
            //     trayIcon.setImage(updatedImage);
            // }

        } catch (UnsupportedOperationException e) { // Current OS does not support tray mode
            // disable tray option in your application or perform other actions
            log.error("트레이 기능을 지원하지 않는 기종에서 실행되었습니다.");
        } catch (AWTException e) { // Failed to add the tray image
            log.error("트레이 아이콘을 추가하는 데 실패했습니다.");
        } catch (Exception e) { // etc.
            log.error("트레이 초기화 중 기타 에러가 발생하셨습니다.", e);
            throw e;
        }

    }

    // Load window and tray icon
    public void initAll() {
        initWindow();
        initTray();
    }

    // Minimalization
    public void goTray() {
        System.out.println("트레이로");
        setVisible(false);
    }

    // Minimalization
    public void activateWindow() {
        System.out.println("윈도 재활성화");
        setVisible(true);
    }

}
