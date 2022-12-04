package cmd;

import jda.Jda;
//import net.dv8tion.jda.api.entities.TextChannel; << 이건 구버전에서 썼었던 모양..
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

// 메세지를 보내는 메소드를 모은 곳
@Slf4j
public class Sender {

	// 메세지 보내는 원본 메소드
	public static void send(TextChannel channel, String msg) {
		log.info("메세지를 보내 보겠습니다. 현재 채널 메세지 발송 가능 여부: " + channel.canTalk());
		if(channel.canTalk()) channel.sendMessage(msg).queue();
		else log.info("메세지를 보낼 수가 없어요");
	}

	// 특정 ID의 채널에 텍스트 채널에 메세지 전송
	public static void send(String channelId, String msg) {
		TextChannel channel = Jda.instance.getTextChannelById(channelId);
		log.info(channelId + " 채널에 메세지를 보냅니다. (채널 존재 여부: " + (channel != null) + ")");
		if (channel != null) {
			send(channel, msg);
		} else {
			log.info("채널이 존재하지 않아 메세지를 보낼 수 없습니다.");
		}
	}

	// 이벤트가 접수된 텍스트 채널에 메세지 전송. 모든 타입의 채널에 가능
	public static void send(MessageReceivedEvent event, String msg) {
		log.info("이벤트가 접수된 채널에 메세지를 보냅니다. (모든 타입의 채널 대응용 메소드 실행)");
		event.getChannel().sendMessage(msg).queue();
	}

}
