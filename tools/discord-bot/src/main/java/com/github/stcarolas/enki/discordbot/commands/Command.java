package com.github.stcarolas.enki.discordbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
    String name();
    String description();
    void handle(MessageReceivedEvent request);
}
