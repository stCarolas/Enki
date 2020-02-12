package com.github.stcarolas.enki.discordbot;

import javax.inject.Singleton;

import com.github.stcarolas.enki.discordbot.commands.CommandsProvider;
import com.github.stcarolas.enki.discordbot.providers.ConfigProvider;
import com.github.stcarolas.enki.discordbot.providers.SessionHandlersProvider;

import dagger.Component;

@Component(
	modules = {
		ConfigProvider.class, SessionHandlersProvider.class, CommandsProvider.class
	}
)
@Singleton
public interface EnkiDiscordBotComponent {
	MessageListener listener();
}
