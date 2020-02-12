package com.github.stcarolas.enki.discordbot;

import javax.security.auth.login.LoginException;
import com.github.stcarolas.enki.discordbot.providers.OrganizationApiProvider;
import com.typesafe.config.ConfigFactory;
import lombok.val;
import net.dv8tion.jda.api.JDABuilder;

public class Launcher {

	public static void main(String[] args) throws LoginException {
		val config = ConfigFactory.load();
		val createParameters = OrganizationApiProvider.Configuration.builder()
			.username(config.getString("gitea.username"))
			.password(config.getString("gitea.password"))
			.giteaUrl(config.getString("gitea.url"))
			.organization(config.getString("gitea.organization"))
			.build();

		new JDABuilder(config.getString("discord.token"))
			.addEventListeners(
				DaggerEnkiDiscordBotComponent.builder()
					.organizationApiProvider(
						new OrganizationApiProvider(createParameters)
					)
					.build()
					.listener()
			)
			.build();
	}
}
