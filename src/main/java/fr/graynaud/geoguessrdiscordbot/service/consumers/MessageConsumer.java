package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;

public interface MessageConsumer {

    String getCommand();

    void consume(Message message, ApplicationProperties applicationProperties);
}
