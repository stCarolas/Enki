package com.github.stcarolas.enki.discordbot.commands.fill;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FillDataWrapperProvider {

	@Provides
	@Singleton
	public Map<String, FillData> provide(){
		return new HashMap<>();
	}
    
}
