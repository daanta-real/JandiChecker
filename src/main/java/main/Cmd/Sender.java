package main.Cmd;

import $.$;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

// 메세지를 보내는 메소드를 모은 곳
public class Sender {

	// 메세지 보내는 원본 메소드
	public static void send(TextChannel channel, String msg) {
		$.pn(channel.canTalk());
		if(channel.canTalk()) channel.sendMessage(msg).queue();
		else $.pn("메세지를 보낼 수가 없어요");
	}

	// 이벤트가 들어왔을 때 메세지 전송
	public static void send(MessageReceivedEvent event, String msg) { event.getChannel().sendMessage(msg).queue(); }
	// 채널명을 입력받았을 경우 메세지 전송
	public static void send(String channelId, String msg) {
//		try {
//
//		}
//		event.getChannel().sendMessage(msg).queue();
	}

}
