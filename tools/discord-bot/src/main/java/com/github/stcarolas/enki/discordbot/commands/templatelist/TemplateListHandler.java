package com.github.stcarolas.enki.discordbot.commands.templatelist;

import java.util.Comparator;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import com.github.stcarolas.enki.discordbot.commands.SessionHandler;
import com.github.stcarolas.enki.discordbot.commands.Writer;
import lombok.val;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import rocks.mango.gitea.OrganizationApi;
import rocks.mango.gitea.Repository;

@Singleton
public class TemplateListHandler implements SessionHandler {
    private final OrganizationApi organizationApi;
    private final String organizationName;
    private final Writer writer;

    @Inject
    public TemplateListHandler(
        OrganizationApi organizationApi,
        Writer writer,
        @Named("giteaOrganization") String organizationName
    ) {
        this.organizationApi = organizationApi;
        this.writer = writer;
        this.organizationName = organizationName;
    }

    @Override
    public String command(MessageReceivedEvent event) {
        return "list";
    }

    @Override
    public String instruction(MessageReceivedEvent event) {
        return "changeme";
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        val message = event.getMessage().getContentRaw().trim();
        if (
            message != null &&
            (message.startsWith("fill") || message.startsWith("template -list"))
        ) {
            writer.writeResponse(event, handle(event.getChannel().getId()));
        }
    }

    @Override
    public String handle(String sessionId) {
        StringBuilder response = new StringBuilder("**__Доступные шаблоны__**");
        organizationApi.orgListRepos(organizationName)
            .stream()
            .filter(repo -> repo.getName().startsWith("template"))
            .sorted(
                new Comparator<Repository>() {
                    @Override
                    public int compare(Repository o1, Repository o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                }
            )
            .forEach(
                repo -> {
                    response.append("\n").append(repo.getName());
                }
            );
        return response.toString();
    }

    @Override
    public void putInSession(String sessionId, String data) {}

    @Override
    public String data(String sessionId) {
        return "";
    }
}
