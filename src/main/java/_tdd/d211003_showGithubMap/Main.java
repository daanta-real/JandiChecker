package _tdd.d211003_showGithubMap;

import $.$;

public class Main { public static void main(String[] args) throws Exception {

	// recentTotal, recentCount, totalMap
	String id = "daanta-real";
	String result = GithubMap.getGithubInfoString("준성", id);
	$.pn(result);

} }