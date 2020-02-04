package com.github.stcarolas.enki.discordbot.commands.create;

import javax.inject.Named;

import com.github.stcarolas.enki.discordbot.providers.ConfigProvider;

import dagger.Module;
import dagger.Provides;

@Module(includes = { ConfigProvider.class })
public class CreateCommandConfigProvider {

    @Provides
    @Named("giteaOrganization")
    public String provideOrganizationName() {
        return "Mango";
    }

    @Provides
    @Named("createCommand")
    public String provideCreateCommand() {
        return "create";
    }
}
