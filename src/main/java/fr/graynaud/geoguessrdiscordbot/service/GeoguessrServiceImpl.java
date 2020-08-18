package fr.graynaud.geoguessrdiscordbot.service;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.consumers.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeoguessrServiceImpl implements GeoguessrService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoguessrServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    private final DiscordClient client;

    private final GatewayDiscordClient gateway;

    private final List<MessageConsumer> consumers;

    public GeoguessrServiceImpl(ApplicationProperties applicationProperties, List<MessageConsumer> consumers) {
        this.applicationProperties = applicationProperties;
        this.client = DiscordClient.create(this.applicationProperties.getDiscordToken());
        this.gateway = this.client.login().block();
        this.consumers = consumers;

        subscribeToMessages();
    }

    private void subscribeToMessages() {
        this.gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();

            if (message.getChannel().block() != null && message.getAuthor().isPresent() && !message.getAuthor().get().isBot()) {
                this.consumers.stream()
                              .filter(consumer -> message.getContent().startsWith(Constants.COMMAND_PREFIX))
                              .filter(consumer -> message.getContent().startsWith(Constants.COMMAND_PREFIX + consumer.getCommand()))
                              .forEach(consumer -> consumer.consume(message, this.applicationProperties));
            }
        });

        this.gateway.onDisconnect().block();
    }
}
