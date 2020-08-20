package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.common.utils.DiscordUtils;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HelpConsumer implements MessageConsumer {

    private final List<MessageConsumer> consumers;

    public HelpConsumer(List<MessageConsumer> consumers) {
        this.consumers = consumers;
    }

    @Override
    public String getDescription() {
        return "Help command, list of commands of the bot.";
    }

    @Override
    public String getExample() {
        return getCommand();
    }

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public void consume(Message message, ApplicationProperties applicationProperties) {
        message.getChannel().block().createEmbed(spec -> {
            DiscordUtils.generalEmbedMessage(spec);
            spec.setDescription(this.consumers.stream().map(DiscordUtils::consumerToHelpDescription).collect(Collectors.joining("\n")));
        }).block();
    }
}
