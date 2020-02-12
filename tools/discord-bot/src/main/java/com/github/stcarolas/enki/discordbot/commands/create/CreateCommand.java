package com.github.stcarolas.enki.discordbot.commands.create;

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
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Log4j2
public class CreateCommand implements Command {
	private final Map<String, List<Handler>> handlers;
	private final Writer writer;
	private final CreateRepoHandler createRepoHandler;
	private final String command;

	@Inject
	public CreateCommand(
		Map<String, List<Handler>> handlers,
		Writer writer,
		CreateRepoHandler createRepoHandler,
		@Named("createCommand") String command
	) {
		this.writer = writer;
		this.handlers = handlers;
		this.createRepoHandler = createRepoHandler;
		this.command = command;
	}

	@Override
	public String name() {
		return command;
	}

	@Override
	public String description() {
		return "Cоздание репозитория в Gitea. Первым параметром задается название репозитория, вторым параметром - имя шаблона.";
	}

	@Override
	public void handle(MessageReceivedEvent request) {
		val message = request.getMessage().getContentRaw();
		val commandArgs = message.replace(name(), "").trim();
		val sessionId = request.getChannel().getId();
		createRepoHandler.putInSession(sessionId, commandArgs);
		handlers.put(
			request.getChannel().getId(),
			Arrays.asList(
				SessionWrapper.builder()
					.writer(writer)
					.handler(createRepoHandler)
					.sessionId(request.getChannel().getId())
					.build()
			)
		);
	}
}
