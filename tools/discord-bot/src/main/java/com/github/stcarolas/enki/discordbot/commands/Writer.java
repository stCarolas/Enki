package com.github.stcarolas.enki.discordbot.commands;

import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import io.vavr.collection.List;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@Log4j2
public class Writer {

    @Inject
    public Writer() {}
    
    public void write(MessageChannel channel, String message) {
        List.ofAll(message.getBytes())
            .sliding(1000, 1000)
            .forEach(
                partialMessage -> {
                    try {
                        val bytes = partialMessage.asJava();
                        val castedBytes = new byte[bytes.size()];
                        for (int i=0; i<bytes.size(); i++){
                            castedBytes[i] = bytes.get(i).byteValue();
                        }
                        String messageToSend = new String(castedBytes);
                        log.info("send response:{}", messageToSend);
                        channel.sendMessage(messageToSend)
                            .submit()
                            .get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            );
    }

    public void writeResponse(MessageReceivedEvent request, String message) {
        write(request.getChannel(), message);
    }
}
