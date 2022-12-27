package ui;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@Slf4j
public final class UIMain extends JFrame {

    // 1. Fields
    private static final UIMain INSTANCE;
    private static final JTextArea TEXTAREA = new JTextArea();

    static {
        try {
            INSTANCE = new UIMain();
        } catch (Exception e) {
            log.info("UIMain 객체 생성 중 오류가 발생했습니다.");
            throw new RuntimeException(e);
        }
    }

    private final JPanel WINDOW;
    private final Image IMAGE;
    private final Font FONT;


    // 2. Constructor
    private UIMain() throws Exception {

        log.info("WindowService 초기화 시작");

        // Set JPanel(main window) instance
        WINDOW = new JPanel();
        log.info("WindowService 인스턴스 생성 완료");

        // Set an image icon
        URL imgUrl = getClass().getClassLoader().getResource("img/windows_16x16.png");
        IMAGE = Toolkit.getDefaultToolkit().getImage(imgUrl);
        log.info("잔디체커 이미지 로드 완료");

        // Set font
        InputStream fontStream = Objects.requireNonNull(getClass().getResourceAsStream("/font.otf"));
        FONT = Font
                .createFont(Font.TRUETYPE_FONT, fontStream)
                .deriveFont(16f); // Make sure to derive the size;
        log.info("폰트 로딩 완료");
        UIManager.getLookAndFeelDefaults().put("defaultFont", FONT);
        log.info("폰트를 디폴트 폰트에 적용 완료");

    }

    // 3. Initializer
    public void init() {

        try {

            // Font
            setFont(FONT);
            add(WINDOW);

            // Icon
            setIconImage(IMAGE);

            // Background Color
            setBackground(Color.DARK_GRAY); // This Content area

            // Layout
            setLayout(new BorderLayout());
            setSize(1400, 800);

            // Top box (= text box)
            TEXTAREA.setEditable(false);
            TEXTAREA.setForeground(new Color(169, 183, 198));
            TEXTAREA.setBackground(new Color(43, 43, 43));
            TEXTAREA.setFont(FONT);
            TEXTAREA.setLineWrap(true);
            add(TEXTAREA, BorderLayout.CENTER);

            // Scrollbar
            JScrollPane scroll = new JScrollPane(
                    TEXTAREA,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            );
            add(scroll);

            // Show
            setVisible(true);

            // Reserve exit button in main window
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Initialize ui Menues
            UIMenu ui = UIMenu.getInstance();
            ui.initTray();

            // Reserve making tray action for clicking minimize button
            addWindowStateListener(e -> {
                if ((e.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED){
                    runGoTray();
                }
            });

        } catch (Exception e) {
            log.error("창 초기화 중 에러가 발생하였습니다.");
            throw e;
        }

    }

    // 4. Getter
    public static UIMain getInstance() {
        return INSTANCE;
    }
    public Image getImage() {
        return IMAGE;
    }
    public static JTextArea getTextarea() { return TEXTAREA; }
    // 4. Actions

    // Minimization
    public void runGoTray() {
        log.info("요청에 의해 윈도우를 트레이로 보냅니다.");
        setVisible(false);
        UIMenu.trayIconOn();
    }

    public void runGoActivate() {
        log.info("요청에 의해 윈도우를 활성화합니다.");
        UIMenu.trayIconOff();
        setVisible(true);
        toFront();
        setState(Frame.NORMAL);
    }

    public void runExit() {
        log.info("요청에 의해 종료합니다.");
        System.exit(0);
    }

}
