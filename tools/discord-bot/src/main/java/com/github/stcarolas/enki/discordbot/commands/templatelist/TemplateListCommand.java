package com.github.stcarolas.enki.discordbot.commands.templatelist;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.github.stcarolas.enki.discordbot.SessionWrapper;
import com.github.stcarolas.enki.discordbot.commands.Command;
import com.github.stcarolas.enki.discordbot.commands.Handler;
import com.github.stcarolas.enki.discordbot.commands.Writer;

import lombok.val;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import rocks.mango.gitea.OrganizationApi;

public class TemplateListCommand implements Command {
	private final Writer writer;
	private final Map<String, List<Handler>> handlers;
	private final OrganizationApi organizationApi;
	private final String organizationName;
	private final TemplateListHandler templateListHandler;

	@Inject
	public TemplateListCommand(
		Map<String, List<Handler>> handlers,
		Writer writer,
		OrganizationApi organizationApi,
		TemplateListHandler templateListHandler,
		@Named("giteaOrganization") String organizationName
	) {
		this.writer = writer;
		this.handlers = handlers;
		this.organizationApi = organizationApi;
		this.organizationName = organizationName;
		this.templateListHandler = templateListHandler;
	}

	@Override
	public String name() {
		return "template -list";
	}

	@Override
	public String description() {
		return "Пересоздание из шаблонов по сохраненным настройкам";
	}

	@Override
	public void handle(MessageReceivedEvent request) {
		val sessionId = request.getChannel().getId();
		val sessionHandlers = handlers.get(sessionId);
		if (sessionHandlers != null && sessionHandlers.contains(templateListHandler)) {
			return;
		}
		handlers.put(
			sessionId,
			Arrays.asList(
				SessionWrapper.builder()
					.writer(writer)
					.handler(templateListHandler)
					.sessionId(sessionId)
					.build()
			)
		);
	}
}
