package fr.graynaud.geoguessrdiscordbot.service.objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateChallengeRequest {

    @JsonProperty("map")
    private final String mapId;

    @JsonProperty("timeLimit")
    private final Integer duration;

    @JsonProperty("isCountryStreak")
    private final boolean isCountryStreak;

    @JsonProperty("forbidMoving")
    private final boolean forbidMoving;

    public CreateChallengeRequest(String mapId) {
        this.mapId = mapId;
        this.duration = null;
        this.isCountryStreak = false;
        this.forbidMoving = false;
    }

    public CreateChallengeRequest(String mapId, Integer duration) {
        this.mapId = mapId;
        this.duration = duration;
        this.isCountryStreak = false;
        this.forbidMoving = false;
    }

    public CreateChallengeRequest(String mapId, Integer duration, boolean isCountryStreak, boolean forbidMoving) {
        this.mapId = mapId;
        this.duration = duration;
        this.isCountryStreak = isCountryStreak;
        this.forbidMoving = forbidMoving;
    }

    public String getMapId() {
        return mapId;
    }

    public Integer getDuration() {
        return duration;
    }

    public boolean isCountryStreak() {
        return isCountryStreak;
    }

    public boolean isForbidMoving() {
        return forbidMoving;
    }
}
