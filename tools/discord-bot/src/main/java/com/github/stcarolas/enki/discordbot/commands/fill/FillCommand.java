package com.github.stcarolas.enki.discordbot.commands.fill;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import com.github.stcarolas.enki.discordbot.SessionWrapper;
import com.github.stcarolas.enki.discordbot.commands.Command;
import com.github.stcarolas.enki.discordbot.commands.Handler;
import com.github.stcarolas.enki.discordbot.commands.Writer;
import com.github.stcarolas.enki.discordbot.commands.fill.handlers.GenerateHandler;
import com.github.stcarolas.enki.discordbot.commands.fill.handlers.GitUrlHandler;
import com.github.stcarolas.enki.discordbot.commands.fill.handlers.TemplateDataHandler;
import com.github.stcarolas.enki.discordbot.commands.fill.handlers.TemplateMappingHandler;
import com.github.stcarolas.enki.discordbot.commands.fill.handlers.TemplateUrlHandler;
import com.github.stcarolas.enki.discordbot.commands.templatelist.TemplateListHandler;

import lombok.val;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Singleton
public class FillCommand implements Command {
	private final GitUrlHandler gitUrlHandler;
	private final TemplateUrlHandler templateUrlHandler;
	private final TemplateDataHandler templateDataHandler;
	private final GenerateHandler generateHandler;
	private final TemplateMappingHandler templateMappingHandler;
	private final Writer writer;
	private final Map<String, List<Handler>> handlers;
	private final TemplateListHandler templateListHandler;

	@Inject
	public FillCommand(
		GitUrlHandler gitUrlHandler,
		TemplateUrlHandler templateUrlHandler,
		TemplateDataHandler templateDataHandler,
		TemplateListHandler templateListHandler,
		TemplateMappingHandler templateMappingHandler,
		GenerateHandler generateHandler,
		Map<String, List<Handler>> handlers,
		Writer writer
	) {
		this.gitUrlHandler = gitUrlHandler;
		this.templateUrlHandler = templateUrlHandler;
		this.handlers = handlers;
		this.templateDataHandler = templateDataHandler;
		this.templateListHandler = templateListHandler;
		this.writer = writer;
		this.templateMappingHandler = templateMappingHandler;
		this.generateHandler = generateHandler;
	}

	@Override
	public String name() {
		return "fill";
	}

	@Override
	public String description() {
		return "Заполнение репозитория по шаблону";
	}

	@Override
	public void handle(MessageReceivedEvent request) {
		val message = request.getMessage().getContentRaw();
		val commandArgs = message.replace(name(), "").trim();
		val sessionId = request.getChannel().getId();
		gitUrlHandler.putInSession(sessionId, commandArgs);
		handlers.put(
			request.getChannel().getId(),
			Arrays.asList(
				templateListHandler,
				SessionWrapper.builder()
					.writer(writer)
					.handler(gitUrlHandler)
					.handler(templateUrlHandler)
					.handler(templateDataHandler)
					.handler(templateMappingHandler)
					.handler(generateHandler)
					.sessionId(request.getChannel().getId())
					.build()
			)
		);
	}
}
