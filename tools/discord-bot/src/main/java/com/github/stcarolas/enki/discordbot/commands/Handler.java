package com.github.stcarolas.enki.discordbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Handler {
    String command(MessageReceivedEvent event);
    String instruction(MessageReceivedEvent event);
    void handle(MessageReceivedEvent event);
}
