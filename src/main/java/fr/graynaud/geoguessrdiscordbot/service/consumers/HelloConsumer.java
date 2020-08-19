package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloConsumer implements MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloConsumer.class);

    @Override
    public String getCommand() {
        return "hello";
    }

    @Override
    public void consume(Message message, ApplicationProperties applicationProperties) {
        if (message.getAuthor().isPresent()) {
            message.getRestChannel().createMessage("Hello " + message.getAuthor().get().getUsername() + " !").block();
        } else {
            message.getRestChannel().createMessage("Hello !").block();
        }
    }
}
