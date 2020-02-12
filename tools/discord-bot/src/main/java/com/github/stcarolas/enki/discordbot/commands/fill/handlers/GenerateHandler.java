package com.github.stcarolas.enki.discordbot.commands.fill.handlers;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.impl.GitRepo;
import com.github.stcarolas.enki.discordbot.commands.SessionHandler;
import com.github.stcarolas.enki.discordbot.commands.fill.FillData;
import com.github.stcarolas.enki.logginganalyzers.Generator;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Builder
@Log4j2
public class GenerateHandler implements SessionHandler {
	private final Map<String, FillData> sessions;

	@Inject
	public GenerateHandler(Map<String, FillData> sessions) {
		this.sessions = sessions;
	}

	@Override
	public void handle(MessageReceivedEvent event) {
		// todo
	}

	@Override
	public String command(MessageReceivedEvent event) {
		return "";
	}

	@Override
	public String instruction(MessageReceivedEvent event) {
		return "";
	}

	@Override
	public String handle(String sessionId) {
		val data = sessions.get(sessionId);
		val generator = Generator.builder()
			.cloneUrl(data.getTemplateUrl())
			.data(data.getValues())
			.mappings(new HashMap(data.getMappins()))
			.saveParameters(true)
			.build();
		val repo = GitRepo.builder()
			.cloneUrl(CloneURLType.SSH, data.getRepoUrl())
			.build();
		generator.analyze(repo);
		sessions.put(sessionId, FillData.builder().values(new HashMap<>()).mappins(new HashMap<>()).build());
		return "Исполнено";
	}

	@Override
	public void putInSession(String sessionId, String data) {}

	@Override
	public String data(String sessionId) {
		return "";
	}
}
