package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PongConsumer implements MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PongConsumer.class);

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public void consume(Message message, ApplicationProperties applicationProperties) {
        message.getRestChannel().createMessage("Pong!").block();
    }
}
