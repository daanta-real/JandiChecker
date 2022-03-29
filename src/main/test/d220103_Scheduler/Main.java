package test.d220103_Scheduler;

public class Main {

	public static void main(String[] args) {

		// 스케쥴러 실행
		Scheduler.run(RunJob.class, "* * * * * ? *");

	}
}
