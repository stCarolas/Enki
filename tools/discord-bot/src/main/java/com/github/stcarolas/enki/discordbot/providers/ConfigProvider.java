package com.github.stcarolas.enki.discordbot.providers;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigProvider {

    @Provides
    public Config provide(){
        return ConfigFactory.load();
    }
    
}
