package cmd;

import $.$;
import jda.Jda;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

// 메세지를 보내는 메소드를 모은 곳
public class Sender {

	// 메세지 보내는 원본 메소드
	public static void send(TextChannel channel, String msg) {
		$.pn("메세지를 보내 보겠습니다. 현재 채널 메세지 발송 가능 여부: " + channel.canTalk());
		if(channel.canTalk()) channel.sendMessage(msg).queue();
		else $.pn("메세지를 보낼 수가 없어요");
	}

	// 특정 ID의 채널에 텍스트 채널에 메세지 전송
	public static void send(String channelId, String msg) {
		TextChannel channel = Jda.instance.getTextChannelById(channelId);
		$.pn(channelId + " 채널에 메세지를 보냅니다. (채널 존재 여부: " + (channel != null) + ")");
		send(channel, msg);
	}

	// 이벤트가 접수된 텍스트 채널에 메세지 전송. 모든 타입의 채널에 가능
	public static void send(MessageReceivedEvent event, String msg) {
		$.pn("이벤트가 접수된 채널에 메세지를 보냅니다. (모든 타입의 채널 대응용 메소드 실행)");
		event.getChannel().sendMessage(msg).queue();
	}

}
