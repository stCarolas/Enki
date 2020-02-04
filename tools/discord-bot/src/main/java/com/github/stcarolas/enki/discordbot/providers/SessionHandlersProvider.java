package com.github.stcarolas.enki.discordbot.providers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import com.github.stcarolas.enki.discordbot.commands.Handler;

import dagger.Module;
import dagger.Provides;

@Module
public class SessionHandlersProvider {

    @Provides
    @Singleton
    public Map<String, List<Handler>> provider() {
        return new HashMap<>();
    }
}
