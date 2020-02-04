package com.github.stcarolas.enki.discordbot;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import com.github.stcarolas.enki.discordbot.commands.Command;
import com.github.stcarolas.enki.discordbot.commands.Handler;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Log4j2
public class MessageListener extends ListenerAdapter {
    private static final String BOT_NAME = "Enki";

    private final Map<String, List<Handler>> sessionHandlers;
    private final Set<Command> commands;

    @Inject
    public MessageListener(
        Map<String, List<Handler>> sessionHandlers,
        Set<Command> commands
    ) {
        this.sessionHandlers = sessionHandlers;
        this.commands = commands;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            handle(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handle(MessageReceivedEvent event) {
        if (noNeedToHandle(event)) {
            return;
        }
        if (seemsLikeCommand(event)) {
            executeCommand(event);
        }
        if (sessionHasHandlers(event)) {
            delegateToHandlers(event);
        }
    }

    private boolean noNeedToHandle(MessageReceivedEvent event) {
        log.info("private channel: `{}`", event.getPrivateChannel());
        return BOT_NAME.equals(event.getAuthor().getName());
    }

    private boolean seemsLikeCommand(MessageReceivedEvent event) {
        return true;
    }

    private void executeCommand(MessageReceivedEvent event) {
        val message = event.getMessage().getContentRaw();
        for (val command : commands) {
            if (
                message.startsWith("!" + command.name()) ||
                message.startsWith(command.name())
            ) {
                log.debug("handle by `{}`", command.name());
                command.handle(event);
            }
        }
    }

    private boolean sessionHasHandlers(MessageReceivedEvent event) {
        log.debug("check for handlers for session `{}` in map size of {}", event.getChannel().getId(), sessionHandlers.size());
        return sessionHandlers.containsKey(event.getChannel().getId());
    }

    private void delegateToHandlers(MessageReceivedEvent event) {
        log.debug("try to delegate  session to handlers");
        sessionHandlers.get(event.getChannel().getId())
            .forEach(
                handler -> {
                    log.debug("delegate  session to {}", handler);
                    handler.handle(event);
                }
            );
    }
}
