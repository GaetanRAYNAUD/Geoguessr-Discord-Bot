package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.common.utils.DiscordUtils;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.GeoguessrService;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class CountryStreakConsumer implements MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryStreakConsumer.class);

    private final GeoguessrService geoguessrService;

    public CountryStreakConsumer(GeoguessrService geoguessrService) {
        this.geoguessrService = geoguessrService;
    }

    @Override
    public String getDescription() {
        return "Generate a game link for a country streak game and duration. How many will you guess in a row ?!";
    }

    @Override
    public String getExample() {
        return getCommand() + " 300 true";
    }

    @Override
    public String getCommand() {
        return "cs";
    }

    @Override
    public void consume(Message message, ApplicationProperties applicationProperties) {
        String content = message.getContent().substring(Constants.COMMAND_PREFIX.length() + getCommand().length()).trim();

        if (content.indexOf(' ') < 0) {
            message.getRestChannel().createMessage("The command require two parameters: duration of the game, can move ! Ex: "
                                                   + Constants.COMMAND_PREFIX + getCommand() + " 300 true").block();
            return;
        }

        String durationString = content.substring(0, content.indexOf(' '));
        Integer duration;

        try {
            duration = Integer.parseInt(durationString);
        } catch (Exception e) {
            message.getRestChannel().createMessage("Could not parse the duration of the game ! Check that you entered a valid number !").block();
            return;
        }

        if (duration < 0) {
            message.getRestChannel().createMessage("The duration of the game needs to be positive !").block();
            return;
        }

        if (duration == 0) {
            duration = null; //Set to null to not send it to Geoguessr
        }

        boolean forbidMoving = Boolean.parseBoolean(content.substring(durationString.length() + 1));

        GeoguessrMap geoguessrMap = new GeoguessrMap(Constants.COUNTRY_STREAK_NAME, Constants.COUNTRY_STREAK_ID, Constants.COUNTRY_STREAK_DESC);

        try {
            String token = this.geoguessrService.getGameToken(geoguessrMap, duration, true, forbidMoving);

            Integer finalDuration = duration;
            message.getChannel()
                   .block()
                   .createEmbed(spec -> DiscordUtils.geoMapToEmbedMessage(spec, geoguessrMap, token, finalDuration, true, forbidMoving))
                   .block(Duration.of(1, ChronoUnit.SECONDS));
        } catch (Exception e) {
            LOGGER.error("An error occurred while creating game {}: {} !", Constants.COUNTRY_STREAK_ID, e.getMessage(), e);
            message.getRestChannel().createMessage("Couldn't create the game ! Check your message again !").block(Duration.of(3, ChronoUnit.SECONDS));
        }
    }
}
