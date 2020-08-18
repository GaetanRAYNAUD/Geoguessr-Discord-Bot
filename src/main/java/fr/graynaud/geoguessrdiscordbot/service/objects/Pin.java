package fr.graynaud.geoguessrdiscordbot.service.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Pin {

    @JsonProperty("url")
    public String url;

    @JsonProperty("anchor")
    public String anchor;

    @JsonProperty("isDefault")
    public Boolean isDefault;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
