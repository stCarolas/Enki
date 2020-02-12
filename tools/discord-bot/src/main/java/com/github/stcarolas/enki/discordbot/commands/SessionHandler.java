package com.github.stcarolas.enki.discordbot.commands;

public interface SessionHandler extends Handler{
	String handle(String sessionId);
	void putInSession(String sessionId, String data);
	String data(String sessionId);
}
