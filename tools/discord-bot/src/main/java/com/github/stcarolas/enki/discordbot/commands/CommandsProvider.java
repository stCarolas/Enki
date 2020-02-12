package com.github.stcarolas.enki.discordbot.commands;

import java.util.Set;
import com.github.stcarolas.enki.discordbot.commands.create.CreateCommand;
import com.github.stcarolas.enki.discordbot.commands.create.CreateCommandConfigProvider;
import com.github.stcarolas.enki.discordbot.commands.fill.FillCommand;
import com.github.stcarolas.enki.discordbot.commands.fill.FillDataWrapperProvider;
import com.github.stcarolas.enki.discordbot.commands.refill.RefillCommand;
import com.github.stcarolas.enki.discordbot.commands.templatelist.TemplateListCommand;
import com.github.stcarolas.enki.discordbot.providers.OrganizationApiProvider;
import dagger.Module;
import dagger.Provides;
import io.vavr.collection.HashSet;

@Module(
	includes = {
		OrganizationApiProvider.class,
		CreateCommandConfigProvider.class,
		FillDataWrapperProvider.class
	}
)
public class CommandsProvider {

	@Provides
	public Set<Command> provide(final CreateCommand createCommand, final TemplateListCommand templateListCommand, final FillCommand fillCommand, final RefillCommand refillCommand) {
		return HashSet.of(createCommand, fillCommand, refillCommand, templateListCommand).toJavaSet();
	}
}
