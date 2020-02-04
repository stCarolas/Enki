package com.github.stcarolas.enki.discordbot;

import java.util.List;

import com.github.stcarolas.enki.discordbot.commands.Handler;
import com.github.stcarolas.enki.discordbot.commands.SessionHandler;
import com.github.stcarolas.enki.discordbot.commands.Writer;

import lombok.Builder;
import lombok.Singular;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Builder
@Log4j2
public class SessionWrapper implements Handler {
    @Singular
    private List<SessionHandler> handlers;

    private Writer writer;
    private String instruction;
    private String sessionId;

    @Override
    public String command(MessageReceivedEvent event) {
        return "changeme";
    }

    @Override
    public String instruction(MessageReceivedEvent event) {
        return instruction;
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        val message = event.getMessage().getContentRaw();
        if ("yes".equals(message)) {
            apply(event);
            return;
        }
        handlers.forEach(
            handler -> {
                val command = handler.command(event);
                if (command.isEmpty()) {
                    return;
                }
                if (message.startsWith(command)) {
                    val commandArgs = message.replaceFirst(command, "").trim();
                    log.info("put in session: {}", commandArgs);
                    handler.putInSession(sessionId, commandArgs);
                }
            }
        );
        String response = "**Используемые параметры**";
        for (val handler: handlers){
            response = response + "\n" + handler.data(sessionId);
        }
        response = response + "\n---\n**Введите `yes` для выполнения**";
        writer.writeResponse(event, response);
    }

    public void apply(MessageReceivedEvent event) {
        handlers.forEach(
            handler -> {
                val response = handler.handle(sessionId);
                if (response != null && !response.isEmpty()) {
                    writer.writeResponse(event, response);
                }
            }
        );
    }
}
