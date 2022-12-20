package UI;

import init.Initializer;
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

    static {
        try {
            INSTANCE = new UIMain();
        } catch (Exception e) {
            log.debug("UIMain 객체 생성 중 오류가 발생했습니다.");
            throw new RuntimeException(e);
        }
    }

    private final JPanel WINDOW;
    private final Image IMAGE;
    private final Font FONT;


    // 2. Constructor
    private UIMain() throws Exception {

        log.debug("WindowService 초기화 시작");

        // Set JPanel(main window) instance
        WINDOW = new JPanel();
        log.debug("WindowService 인스턴스 생성 완료");

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

    // 3. Initializer
    public void init() throws Exception {

        try {

            // Title
            setTitle("잔디체커 " + Initializer.VERSION + " Build " + Initializer.BUILD);
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

            // Show
            setVisible(true);

            // Reserve exit button in main window
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Initialize UI Menues
            UIMenu ui = UIMenu.getInstance();
            ui.initTray();

            // Reserve making tray icon for clicking minimize button
            addWindowStateListener(e -> {
                if ((e.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED){
                    log.debug("최소화 버튼을 눌러 최소화합니다.");
                    ui.runGoTray();
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

    // 5. Methods

    // Returns image
    public Image getImage() {
        return IMAGE;
    }

    // Minimalization
    public void activateWindow() {
        System.out.println("윈도 재활성화");
        setVisible(true);
    }

}
