package main.cmd;

import main.crawler.Crawler;

public class Cmd {

	// 1명의 깃헙 정보를 출력
	public static void showJandiMap(String id) throws Exception {
		Crawler.getGithubMap(id);
	}

}