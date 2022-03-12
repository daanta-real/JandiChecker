package main;

import $.$;
import main.data.MainSystem;
import main.libraries.Jda;

class Main {

	// 로딩모듈
	public static void ready() {

		$.pn("[[[잔디체커 시작]]]");

		// 환경설정 로드
		$.pn("\n[[[잔디체커 환경설정 로드]]]\n");
		MainSystem.load();

		// JDA 로드
		$.pn("\n[[[잔디체커 JDA 로드]]]\n");
		Jda.load();

		$.pn("\n[[[잔디체커 실행 완료]]]\n");

	}

	// 서버의 실행
	public static void main(String[] args) throws Exception {

		ready();
		/*TextChannel textChannel = YourBotInstance.getJda().getTextChannelById("386242731875368960");
		if(textChannel.canTalk()) {
		    textChannel.sendMessage("Your message here.").queue();
		}*/

	}

}
