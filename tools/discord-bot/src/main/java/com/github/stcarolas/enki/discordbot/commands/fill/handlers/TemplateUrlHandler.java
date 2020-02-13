package com.github.stcarolas.enki.discordbot.commands.fill.handlers;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.impl.GitRepo;
import com.github.stcarolas.enki.discordbot.commands.SessionHandler;
import com.github.stcarolas.enki.discordbot.commands.fill.FillData;
import com.github.stcarolas.enki.logginganalyzers.GenerationParameters;
import com.google.gson.Gson;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Builder
@Log4j2
public class TemplateUrlHandler implements SessionHandler {
	private Map<String, FillData> sessions;

	@Inject
	public TemplateUrlHandler(Map<String, FillData> sessions) {
		this.sessions = sessions;
	}

	@Override
	public void handle(MessageReceivedEvent event) {
		val templateUrl = event.getMessage().getContentRaw().trim();
		val sessionId = event.getChannel().getId();
		putInSession(sessionId, templateUrl);
		handle(sessionId);
	}

	@Override
	public String command(MessageReceivedEvent event) {
		return "template";
	}

	@Override
	public String instruction(MessageReceivedEvent event) {
		return "changeme";
	}

	@Override
	public String handle(String sessionId) {
		return null;
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
		sessions.put(sessionId, builder.templateUrl(data).build());
		loadDefault(sessionId,data);
	}

	private void loadDefault(String sessionId, String repo) {
		val templateRepo = GitRepo.builder().cloneUrl(CloneURLType.SSH, repo).build();
		templateRepo.getDirectory()
			.ifPresent(
				dir -> {
					val defaultValues = new File(dir.toString() + "/default.json");
					if (defaultValues.exists()) {
						log.info("Load settings from {}", defaultValues.toString());
						val gson = new Gson();
						Try.of(
							() -> {
								return gson.fromJson(
									new FileReader(defaultValues),
									GenerationParameters.class
								);
							}
						)
							.onSuccess(
								parameters -> {
									log.info("Loaded parameters: {}", parameters);
									val data = sessions.get(sessionId).toBuilder()
										.values(new HashMap<>(parameters.getData()))
										.mappins(new HashMap<>(parameters.getMapping()))
										.build();
									sessions.put(sessionId, data);
								}
							)
							.onFailure(
								error -> log.error("Error in RetroGenerator: {}", error)
							);
					}
				}
			);
	}

	@Override
	public String data(String sessionId) {
		String url = "";
		if (sessions.containsKey(sessionId)) {
			url = sessions.get(sessionId).getTemplateUrl();
		}
		return "__template__ " +
			((url == null || url.isEmpty())
				? "<введите имя репозитория с шаблоном( или ssh git url)>"
				: url);
	}
}
