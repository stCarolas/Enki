package com.github.stcarolas.enki.discordbot.commands.create;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import com.github.stcarolas.enki.discordbot.commands.SessionHandler;
import com.github.stcarolas.enki.discordbot.commands.Writer;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import rocks.mango.gitea.CreateRepoOption;
import rocks.mango.gitea.OrganizationApi;

@Log4j2
public class CreateRepoHandler implements SessionHandler {
    private final OrganizationApi organizationApi;
    private final Writer writer;
    private final String organization;
    private final Map<String, String> sessionData = new HashMap<>();

    @Inject
    public CreateRepoHandler(
        OrganizationApi organizationApi,
        Writer writer,
        @Named("giteaOrganization") String organization
    ){
        this.organizationApi = organizationApi;
        this.writer = writer;
        this.organization = organization;
    }

    @Override
    public String command(MessageReceivedEvent event) {
        return "name"; 
    }

    @Override
    public String instruction(MessageReceivedEvent event) {
        return "changeme";
    }

    @Override
    public void putInSession(String sessionId, String data) {
        sessionData.put(sessionId, data);
    }

    @Override
    public String handle(String sessionId) {
        val repoName = sessionData.get(sessionId);
        if (repoName.isEmpty()) {
            return "Введите имя репозитория";
        }
        log.info("Creating: {}", repoName);
        val newRepo = organizationApi.createOrgRepo(
            organization,
            new CreateRepoOption().name(repoName)
        );
        return "Новый репозиторий создан : " + newRepo.getHtmlUrl();
    }

    @Override
    public void handle(MessageReceivedEvent request) {
        val repoName = request.getMessage().getContentRaw().trim();
        val sessionId = request.getChannel().getId();
        putInSession(sessionId, repoName);
        writer.writeResponse(request, sessionId);
    }

	@Override
	public String data(String sessionId) {
        val repoName = sessionData.get(sessionId);
		return "__name__ " + (repoName.isEmpty() ? "<введите имя репозотория>" : repoName);
	}
}
