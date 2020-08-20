package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import org.springframework.stereotype.Component;

@Component
public class HelloConsumer implements MessageConsumer {

    @Override
    public String getDescription() {
        return "Hello !";
    }

    @Override
    public String getExample() {
        return getCommand();
    }

    @Override
    public String getCommand() {
        return "hello";
    }

    @Override
    public void consume(Message message, ApplicationProperties applicationProperties) {
        if (message.getAuthor().isPresent()) {
            message.getRestChannel().createMessage("Hello <@" + message.getAuthor().get().getId().asString() + "> !").block();
        } else {
            message.getRestChannel().createMessage("Hello !").block();
        }
    }
}
