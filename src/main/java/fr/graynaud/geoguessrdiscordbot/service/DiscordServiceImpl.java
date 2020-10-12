package fr.graynaud.geoguessrdiscordbot.service;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.consumers.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Objects;

@Service
public class DiscordServiceImpl implements DiscordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    private final GatewayDiscordClient gateway;

    private final List<MessageConsumer> consumers;

    public DiscordServiceImpl(ApplicationProperties applicationProperties, List<MessageConsumer> consumers) {
        this.applicationProperties = applicationProperties;
        this.gateway = DiscordClient.create(this.applicationProperties.getDiscordToken()).login().block();
        this.consumers = consumers;

        subscribeToMessages();
    }

    @PreDestroy
    public void onDestroy() {
        this.consumers.forEach(MessageConsumer::onLogout);
        this.gateway.logout();
    }

    private void subscribeToMessages() {
        this.gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();

            try {
                if (message.getChannel().block() != null && message.getAuthor().isPresent() && !message.getAuthor().get().isBot()) {
                    this.consumers.stream()
                                  .filter(consumer -> message.getContent().toLowerCase().startsWith(Constants.COMMAND_PREFIX))
                                  .filter(consumer -> message.getContent().toLowerCase().startsWith(Constants.COMMAND_PREFIX + consumer.getCommand()))
                                  .forEach(consumer -> consumer.consume(message, this.applicationProperties));
                }
            } catch (Exception e) {
                LOGGER.error("An error occurred: {} !", e.getMessage(), e);
            }
        });

        this.gateway.onDisconnect().block();
    }
}
