package com.github.stcarolas.enki.discordbot.commands.refill;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import com.github.stcarolas.enki.discordbot.SessionWrapper;
import com.github.stcarolas.enki.discordbot.commands.Command;
import com.github.stcarolas.enki.discordbot.commands.Handler;
import com.github.stcarolas.enki.discordbot.commands.Writer;
import lombok.val;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RefillCommand implements Command {
	private final RecreateHandler handler;
	private final Writer writer;
	private final Map<String, List<Handler>> handlers;

	@Inject
	public RefillCommand(
		Map<String, List<Handler>> handlers,
		Writer writer,
		RecreateHandler handler
	) {
		this.handler = handler;
		this.writer = writer;
		this.handlers = handlers;
	}

	@Override
	public String name() {
		return "refill";
	}

	@Override
	public String description() {
		return "Пересоздание из шаблонов по сохраненным настройкам";
	}

	@Override
	public void handle(MessageReceivedEvent request) {
		val message = request.getMessage().getContentRaw();
		val commandArgs = message.replace(name(), "").trim();
		val sessionId = request.getChannel().getId();
		handler.putInSession(sessionId, commandArgs);
		handlers.put(
			request.getChannel().getId(),
			Arrays.asList(
				SessionWrapper.builder()
					.writer(writer)
					.handler(handler)
					.sessionId(request.getChannel().getId())
					.build()
			)
		);
	}
}
