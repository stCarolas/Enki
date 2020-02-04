package com.github.stcarolas.enki.discordbot.commands.refill;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.stcarolas.enki.core.CloneURLType;
import com.github.stcarolas.enki.core.impl.GitRepo;
import com.github.stcarolas.enki.discordbot.commands.SessionHandler;
import com.github.stcarolas.enki.logginganalyzers.RetroGenerator;

import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Log4j2
@Singleton
public class RecreateHandler implements SessionHandler {
    private final Map<String, String> sessions;

    @Inject
    public RecreateHandler() {
        this.sessions = new HashMap<>();
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        log.info("Start handle `recreate` command");
        val message = event.getMessage().getContentRaw().trim();
        val sessionId = event.getChannel().getId();
        putInSession(sessionId, message);
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
        new RetroGenerator()
            .analyze(GitRepo.builder().cloneUrl(CloneURLType.SSH, sessions.get(sessionId)).build());
        return "Исполнено";
    }

    @Override
    public void putInSession(String sessionId, String data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        if (!data.startsWith("ssh")) {
            data = "ssh://git@git.service.consul:2222/Mango/" + data + ".git";
        }
        log.info("Start `recreate` for {}", data);
        sessions.put(sessionId, data);
    }

    @Override
    public String data(String sessionId) {
        String repoUrl = "";
        if (sessions.containsKey(sessionId)) {
            repoUrl = sessions.get(sessionId);
        }
        return "__repo__ " +
            ((repoUrl == null || repoUrl.isEmpty())
                ? "<введите ssh git url от репозитория>"
                : repoUrl);
    }
}
