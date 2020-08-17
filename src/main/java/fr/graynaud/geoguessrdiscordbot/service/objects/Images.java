
package fr.graynaud.geoguessrdiscordbot.service.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Images {

    @JsonProperty("backgroundLarge")
    private String backgroundLarge;

    @JsonProperty("incomplete")
    private Boolean incomplete;

    public String getBackgroundLarge() {
        return backgroundLarge;
    }

    public void setBackgroundLarge(String backgroundLarge) {
        this.backgroundLarge = backgroundLarge;
    }

    public Boolean getIncomplete() {
        return incomplete;
    }

    public void setIncomplete(Boolean incomplete) {
        this.incomplete = incomplete;
    }
}
