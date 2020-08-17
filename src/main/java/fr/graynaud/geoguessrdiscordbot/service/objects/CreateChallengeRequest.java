package fr.graynaud.geoguessrdiscordbot.service.objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateChallengeRequest {

    @JsonProperty("map")
    private final String mapId;

    @JsonProperty("timeLimit")
    private final Integer duration;

    public CreateChallengeRequest(String mapId) {
        this.mapId = mapId;
        this.duration = null;
    }

    public CreateChallengeRequest(String mapId, Integer duration) {
        this.mapId = mapId;
        this.duration = duration;
    }

    public String getMapId() {
        return mapId;
    }

    public Integer getDuration() {
        return duration;
    }
}
