package _tdd.d211003_checker;

import $.$;

public class Main { public static void main(String[] args) throws Exception {
	/*String id = "daanta-real";
	String day = "2021-10-03";
	boolean isCommited = Checker.getIsGithubCommitedByDay(id, day);
	$.pn("\n\n" + isCommited);*/


	// 오늘 날짜 포맷 준비
	boolean s = Checker.getIsGithubCommitedByDay("daanta-real", "2021-10-03");
	$.pn("\n"+s);

	// 전체 확인
	$.pn("어제의 커밋을 전부 점검하겠습니다.");
	Checker.getNotCommitedYesterday();

} }