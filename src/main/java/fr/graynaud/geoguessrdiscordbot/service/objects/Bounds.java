
package fr.graynaud.geoguessrdiscordbot.service.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bounds {

    @JsonProperty("min")
    private LatLng min;

    @JsonProperty("max")
    private LatLng max;

    public LatLng getMin() {
        return min;
    }

    public void setMin(LatLng min) {
        this.min = min;
    }

    public LatLng getMax() {
        return max;
    }

    public void setMax(LatLng max) {
        this.max = max;
    }
}
