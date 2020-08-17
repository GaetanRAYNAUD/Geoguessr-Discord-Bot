package fr.graynaud.geoguessrdiscordbot.service;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import fr.graynaud.geoguessrdiscordbot.common.Constants;
import fr.graynaud.geoguessrdiscordbot.config.ApplicationProperties;
import fr.graynaud.geoguessrdiscordbot.service.objects.CreateChallengeRequest;
import fr.graynaud.geoguessrdiscordbot.service.objects.CreateChallengeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoguessrServiceImpl implements GeoguessrService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoguessrServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    private final DiscordClient client;

    private final GatewayDiscordClient gateway;

    public GeoguessrServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        this.client = DiscordClient.create(this.applicationProperties.getDiscordToken());
        this.gateway = this.client.login().block();

        subscribeToMessages();
    }

    private void subscribeToMessages() {
        this.gateway.on(MessageCreateEvent.class).subscribe(event -> {
            final Message message = event.getMessage();
            final MessageChannel channel = message.getChannel().block();

            if (channel != null) {
                if ("!ping".equals(message.getContent())) {
                    channel.createMessage("Pong!").block();
                } else if (message.getContent().startsWith("!play")) {
                    this.generateGeoguessrLink(message.getContent().substring("!play".length()).trim(), channel);
                }
            }
        });

        this.gateway.onDisconnect().block();
    }

    private void generateGeoguessrLink(String message, MessageChannel channel) {
        String[] words = message.split(" ");

        CreateChallengeRequest createChallengeBody = new CreateChallengeRequest(words[0], Integer.parseInt(words[1]));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.COOKIE, Constants._NCFA + "=" + this.applicationProperties.getGeoguessrNcfa());

        HttpEntity<CreateChallengeRequest> httpEntity = new HttpEntity<>(createChallengeBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CreateChallengeResponse> response = restTemplate.exchange(Constants.GENERATE_TOKEN_URL, HttpMethod.POST, httpEntity,
                                                                                 CreateChallengeResponse.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            channel.createMessage(Constants.CHALLENGE_URL + response.getBody().getToken()).block();
        } else {
            LOGGER.error("An error occurred while getting challenge from Geoguessr: {} !", response.toString());
        }
    }
}
