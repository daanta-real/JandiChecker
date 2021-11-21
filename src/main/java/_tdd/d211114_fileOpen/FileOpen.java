package _tdd.d211114_fileOpen;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Set;

import $.$;
import main.data.DataSystem;

public class FileOpen {

	// DollarCode과 기실습 자료에서 소스를 갖고 왔다. 사용할 수 있는 코드로 추릴 필요가 있다.


    // 파일이름을 넣으면 파일내용을 String으로 돌려주는 메소드
    private static String getFileContents(String fileName) {
    	StringBuilder sb = null;
    	String result = "";
        try {
        	// 파일 읽기 준비
        	// 파일 객체 및 입력 스트림 생성
        	File file = new File(fileName);
        	FileReader frdr = new FileReader(file);

        	// 파일 읽기
        	// 스트링빌더 제작
        	sb = new StringBuilder();
        	// 커서 0에 놓고 모든 문자 읽기
        	for(int curr = 0; (curr = frdr.read()) != -1; ) sb.append((char)curr);
        	frdr.close();

        	// 파일 읽기 종료
        	result = sb.toString();
        }
        catch (FileNotFoundException e) { e.getStackTrace(); return null; }
        catch (IOException e) { e.getStackTrace(); return null; }
        return result;
    }

	// 파일불러오기
	ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(DataSystem.PATH, "lotto.txt"))));
	@SuppressWarnings("unchecked")
	Set<Integer> sRead = (Set<Integer>) ois.readObject(); // Integer형으로 불러옴
	$.pn("읽어온 Integer 값: " + sRead);
	ois.close();

}
