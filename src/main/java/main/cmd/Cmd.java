package main.cmd;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import main.crawler.Crawler;

public class Cmd {

	public static Map<Calendar, Boolean> chkGitActivities(String id) throws Exception {
		Map<Calendar, Boolean> map = new TreeMap<>();
		map = Crawler.getGithubMap(id);
		return map;
	}

}