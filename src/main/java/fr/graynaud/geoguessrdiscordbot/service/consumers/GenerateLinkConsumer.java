package fr.graynaud.geoguessrdiscordbot.service.consumers;

import discord4j.core.object.entity.Message;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.common.utils.DiscordUtils;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.MapsCache;
import fr.graynaud.geoguessrdiscordbot.service.objects.GeoguessrMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GenerateLinkConsumer implements MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateLinkConsumer.class);

    private final RestTemplate restTemplate = new RestTemplate();

    private final MapsCache mapsCache;

    public GenerateLinkConsumer(MapsCache mapsCache) {
        this.mapsCache = mapsCache;
    }

    @Override
    public String getCommand() {
        return "play";
    }

    @Override
    public void consume(Message message, ApplicationProperties applicationProperties) {
        String content = message.getContent().substring(Constants.COMMAND_PREFIX.length() + getCommand().length()).trim();

        if (content.indexOf(' ') < 0) {
            message.getRestChannel().createMessage("The command require two parameters: map name, duration of the game ! Ex: "
                                                   + Constants.COMMAND_PREFIX + getCommand() + " world 300").block();
            return;
        }

        String map = content.substring(0, content.lastIndexOf(' '));

        Integer duration;

        try {
            duration = Integer.parseInt(content.substring(map.length() + 1));
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

        GeoguessrMap geoguessrMap = this.mapsCache.getBySlug(map);

        if (geoguessrMap == null) {
            geoguessrMap = this.mapsCache.getByName(map);
        }

        Integer finalDuration = duration;
        GeoguessrMap finalGeoguessrMap = geoguessrMap;
        message.getChannel()
               .block()
               .createEmbed(spec -> DiscordUtils.geoMapToEmbedMessage(spec, finalGeoguessrMap, "test", finalDuration))
               .block();

        /*CreateChallengeRequest createChallengeBody = new CreateChallengeRequest(map, duration);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.COOKIE, Constants._NCFA + "=" + applicationProperties.getGeoguessrNcfa());

        HttpEntity<CreateChallengeRequest> httpEntity = new HttpEntity<>(createChallengeBody, headers);

        ResponseEntity<CreateChallengeResponse> response;

        try {
            response = this.restTemplate.exchange(Constants.GENERATE_TOKEN_URL, HttpMethod.POST, httpEntity, CreateChallengeResponse.class);
        } catch (HttpClientErrorException.BadRequest e) {
            if (e.getMessage() != null && e.getMessage().contains("InvalidParameters")) {
                LOGGER.error("An error occurred while getting challenge from Geoguessr: {} !", e.getMessage(), e);
                message.getRestChannel().createMessage("The map __**" + map + "**__ does not exist ! Check your message again !").block();
            } else {
                LOGGER.error("An error occurred while getting challenge from Geoguessr: {} !", e.getMessage(), e);
                message.getRestChannel().createMessage("Couldn't create the game ! Check your message again !").block();
            }

            return;
        } catch (Exception e) {
            LOGGER.error("An error occurred while getting challenge from Geoguessr: {} !", e.getMessage(), e);
            message.getRestChannel().createMessage("Couldn't create the game ! Check your message again !").block();
            return;
        }

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            GeoguessrMap geoguessrMap = this.mapsCache.getBySlug(map);
            Integer finalDuration = duration;

            message.getChannel()
                   .block()
                   .createEmbed(spec -> DiscordUtils.geoMapToEmbedMessage(spec, geoguessrMap, response.getBody().getToken(), finalDuration))
                   .block();
        } else {
            LOGGER.error("An error occurred while getting challenge from Geoguessr: {} !", response.toString());
            message.getRestChannel().createMessage("Couldn't create the game ! Check your message again !").block();
        }*/
    }
}
