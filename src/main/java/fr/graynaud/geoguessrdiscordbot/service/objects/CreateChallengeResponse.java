package fr.graynaud.geoguessrdiscordbot.service.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateChallengeResponse {

    @JsonProperty("token")
    private final String token;

    @JsonCreator
    public CreateChallengeResponse(@JsonProperty("token") String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
