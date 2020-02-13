package com.github.stcarolas.enki.discordbot.commands.fill.handlers;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.github.stcarolas.enki.discordbot.commands.SessionHandler;
import com.github.stcarolas.enki.discordbot.commands.fill.FillData;

import lombok.val;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GitUrlHandler implements SessionHandler {
	private final Map<String, FillData> sessions;

	@Inject
	public GitUrlHandler(Map<String, FillData> sessions) {
		this.sessions = sessions;
	}

	@Override
	public void handle(MessageReceivedEvent event) {
		val gitUrl = event.getMessage().getContentRaw().trim();
		val sessionId = event.getChannel().getId();
		putInSession(sessionId, gitUrl);
		handle(sessionId);
	}

	@Override
	public String command(MessageReceivedEvent event) {
		return "repo";
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
		if (data == null || data.isEmpty()){
			return;
		}
		FillData.FillDataBuilder builder;
		if (sessions.containsKey(sessionId)) {
			builder = sessions.get(sessionId).toBuilder();
		} else {
			builder = FillData.builder().values(new HashMap<>()).mappins(new HashMap<>());
		}
		sessions.put(sessionId, builder.repoUrl(data).build());
	}

	@Override
	public String data(String sessionId) {
		String repoUrl = "";
		if (sessions.containsKey(sessionId)) {
			repoUrl = sessions.get(sessionId).getRepoUrl();
		}
		return "__repo__ " +
			((repoUrl == null || repoUrl.isEmpty())
				? "<введите ssh git url от репозитория>"
				: repoUrl);
	}
}
