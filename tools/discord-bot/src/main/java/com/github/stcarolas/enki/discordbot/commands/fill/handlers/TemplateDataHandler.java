package com.github.stcarolas.enki.discordbot.commands.fill.handlers;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import com.github.stcarolas.enki.discordbot.commands.SessionHandler;
import com.github.stcarolas.enki.discordbot.commands.fill.FillData;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Builder
@Log4j2
public class TemplateDataHandler implements SessionHandler {
	private final Map<String, FillData> sessions;

	@Inject
	public TemplateDataHandler(Map<String, FillData> sessions) {
		this.sessions = sessions;
	}

	@Override
	public void handle(MessageReceivedEvent event) {
		val message = event.getMessage().getContentRaw().trim();
		val sessionId = event.getChannel().getId();
		putInSession(sessionId, message);
		handle(sessionId);
	}

	@Override
	public String command(MessageReceivedEvent event) {
		return "data";
	}

	@Override
	public String instruction(MessageReceivedEvent event) {
		return "changeme";
	}

	@Override
	public String handle(String sessionId) {
		return "";
	}

	@Override
	public void putInSession(String sessionId, String data) {
		if (data == null || data.isEmpty()) {
			return;
		}
		FillData.FillDataBuilder builder;
		if (sessions.containsKey(sessionId)) {
			builder = sessions.get(sessionId).toBuilder();
		} else {
			builder = FillData.builder().values(new HashMap<>()).mappins(new HashMap<>());
		}
		val index = data.indexOf('=');
		val key = data.substring(0, index).trim();
		val value = data.substring(index + 1).trim();
		if (key.isEmpty() || value.isEmpty()){
			return;
		}
		log.info("{} : {}", key, value);
		sessions.put(sessionId, builder.value(key, value).build());
	}

	@Override
	public String data(String sessionId) {
		Map<String,String> templateData = new HashMap<>();
		if (sessions.containsKey(sessionId)) {
			templateData = sessions.get(sessionId).getValues();
		}
		String data  = "__data__ ";
		if (templateData.isEmpty()){
			data += "\n<Введите значения шаблона в формате key=value>";
		}
		for (val parameter: templateData.keySet()){
			val value = templateData.get(parameter);
			data += "\n" + parameter + " = " + (( value == null || value.isEmpty())
				? "<введите значения для переменной " + parameter + " >"
				: value);
		}
		return data;
	}
}
