package jda;

//import net.dv8tion.jda.api.entities.TextChannel; << It seems this was used in older version of JDA..

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import utils.CommonUtils;

// The methods about sending messages
@Slf4j
public class JDAMsgService {

	// The core method of sending message
	public static void send(TextChannel channel, String msg) {
		log.info("Sending message. Current message sending availablity: {}", channel.canTalk());
		if(channel.canTalk()) {
			msg = msgTrim(msg);
			msg = CommonUtils.unescapeHTMLEntity(msg);
			channel.sendMessage(msg).queue();
		} else {
			log.info("Can't send the message.");
		}
	}

	// Send the message to specific text channel ID
	public static void send(String channelId, String msg) {
		TextChannel channel = JDAController.instance.getTextChannelById(channelId);
		log.info("Sending the message to channelId {} (channel exists: {})", channelId, channel != null);
		if (channel != null) {
			msg = msgTrim(msg);
			msg = CommonUtils.unescapeHTMLEntity(msg);
			send(channel, msg);
		} else {
			log.info("Couldn't send the message because the channel does not exist.");
		}
	}

	// Send message to the channel which requested the command execution.
	// This can be used for all types of the channel
	public static void send(MessageReceivedEvent event, String msg) {
		msg = msgTrim(msg);
		msg = CommonUtils.unescapeHTMLEntity(msg);
		log.info("Send message to the requested channel: {}", msg);
		event.getChannel().sendMessage(msg).queue();
	}

	// Return trimmed msg.
	// Check the msg length is over 2000.
	// If then, trim it, replace "..." at the end of it, considering markdown backticks.
	public static String msgTrim(String msg) {

		if(StringUtils.isEmpty(msg) || msg.length() < 2000) return msg;

		// If msg length is over 2000 then trim and replace "..." at the end of it
		String last3Chars = msg.substring(msg.length() - 3);
		boolean finishedWithCode = last3Chars.equals("```");
		int cutLength = finishedWithCode ? 1997 : 2000;
		cutLength -= 3;

		String trimmedStr = msg.substring(0, cutLength);
		if(finishedWithCode) trimmedStr += "```";
		trimmedStr += "...";

		return trimmedStr;

	}


}
