package $;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class $ {

	// print
	public static void pr (Object o)                       { System.out.print(o);             }
	// println
	public static void pn ()                               { System.out.println();            }
	public static void pn (Object o)                       { System.out.println(o);           }
	// printf
	public static void pf (String format, Object ... args) { System.out.printf(format, args); }

	// print
	public static void er (Object o)                       { System.err.print(o);             }
	// println
	public static void en ()                               { System.err.println();            }
	public static void en (Object o)                       { System.err.println(o);           }
	// printf
	public static void ef (String format, Object ... args) { System.err.printf(format, args); }

	// returns ①, ②, ③, ... as you input 0, 1, 2, ...
	public static char n(int number) { return (char)(9312 + number); }

    // 원하는 String 경고창으로 표시해줌
	@SuppressWarnings("unused")
	private void alert(String msg) { JOptionPane.showMessageDialog( null, msg, "Alert", JOptionPane.WARNING_MESSAGE ); }

    // 저장할 폴더경로+파일명 문자열을 GUI로 받아서 리턴해줌, 실패시 null 리턴
    public static String fileGetName(String ext, String extDescription) {

    	// 파일선택기 생성
    	// 파일탐색기 기본경로 설정: 기본은 다운로드 폴더
    	// 파일선택기 필터 설정
    	JFileChooser c = new JFileChooser();
        c.setCurrentDirectory(new File ( System.getProperty("user.dir") + System.getProperty("file.separator") + "Download"));
    	c.setFileFilter(new FileNameExtensionFilter(extDescription, ext));

    	// 파일 저장 다이얼로그 출력
    	// 창의 조작 결과에 따른 대응
    	// 1. 창을 끄거나 취소누르면, null값 리턴
    	// 2. 사용자가 파일을 선택하고 "선택" 버튼을 누른 경우, 파일 경로명 성공적으로 리턴
        int ret = c.showSaveDialog(null);
        if(ret != JFileChooser.APPROVE_OPTION) return null;
        else {
        	String filename = c.getSelectedFile().getPath();
            return filename + (!filename.endsWith("." + ext) ? ("." +ext) : "");
        }

    }

	// String을 경로의 파일로 출력. 성공시 true 실패시 false 리턴
    public static boolean fileSave(String fullPath, String contents) { try {
		if(fullPath == null) throw new IOException();
		BufferedWriter bw = new BufferedWriter(new FileWriter(fullPath));
		bw.write(contents); bw.flush(); bw.close();
		return true;
	} catch (Exception e) { return false; } }

    // Returns the size string using kB, MB, Gb, ... format
    public static String getSizeFormatedTxt(long fileSize) {
      Object[][] sizeInfoes = { {1099511627776L, "T"}, {1073741824L, "G"}, {1048576L, "M"}, {1024L, "k"} };
      String s = "";
      for(Object[] o: sizeInfoes) if(fileSize >= (Long)o[0]) {
          s = String.format("%,.2f %s", (float)fileSize / (Long)o[0], o[1] );
          break;
      }
      if(s == "") s = String.format("%,d ", fileSize);
      return s + "Bytes";
    }

    // Returns a Calendar Instance (Which is set by inputted date info)
    public static Calendar getCalendar(String y, String m, String d) {
    	return getCalendar(Integer.valueOf(y), Integer.valueOf(m), Integer.valueOf(d));
    }
    public static Calendar getCalendar(int y, int m, int d) {
    	Calendar c = Calendar.getInstance();
    	c.set(y, m, d);
    	return c;
    }

	// 구글 번역 안내 달기 스트링
	public static final String getGoogleTR_str = "<style type=\"text/css\">\r\n"
		+ "<!--\r\n"
		+ "#goog-gt-tt {display:none !important;}\r\n"
		+ ".goog-te-banner-frame {display:none !important;}\r\n"
		+ ".goog-te-menu-value:hover {text-decoration:none !important;}\r\n"
		+ ".goog-te-gadget-icon {background-image:url(//gtranslate.net/flags/gt_logo_19x19.gif) !important;background-position:0 0 !important;}\r\n"
		+ "body {top:0 !important;}\r\n"
		+ "-->\r\n"
		+ "</style>\r\n"
		+ "<div id=\"google_translate_element\"></div>\r\n"
		+ "<script type=\"text/javascript\">\r\n"
		+ "function googleTranslateElementInit() {new google.translate.TranslateElement({pageLanguage: 'ko', layout: google.translate.TranslateElement.InlineLayout.SIMPLE,autoDisplay: false, includedLanguages: ''}, 'google_translate_element');}\r\n"
		+ "</script><script type=\"text/javascript\" src=\"https://translate.google.com/translate_a/element.js?cb=googleTranslateElementInit\"></script>";

}