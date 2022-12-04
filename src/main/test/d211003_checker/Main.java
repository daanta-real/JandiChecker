package test.d211003_checker;


@Slf4j
public class Main { public static void main(String[] args) throws Exception {
	/*String id = "daanta-real";
	String day = "2021-10-03";
	boolean isCommited = Checker.getIsGithubCommitedByDay(id, day);
	log.info("\n\n" + isCommited);*/


	// 오늘 날짜 포맷 준비
	boolean s = Checker.getIsGithubCommitedByDay("daanta-real", "2021-10-03");
	log.info("\n"+s);

	// 전체 확인
	log.info("어제의 커밋을 전부 점검하겠습니다.");
	Checker.getNotCommitedYesterday();

} }