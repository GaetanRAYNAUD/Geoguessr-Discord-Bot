package fr.graynaud.geoguessrdiscordbot.service.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Creator {

    @JsonProperty("email")
    public String email;

    @JsonProperty("nick")
    public String nick;

    @JsonProperty("isProUser")
    public Boolean isProUser;

    @JsonProperty("pin")
    public Pin pin;

    @JsonProperty("color")
    public Integer color;

    @JsonProperty("url")
    public String url;

    @JsonProperty("id")
    public String id;

    @JsonProperty("stats")
    public Object stats;

    @JsonProperty("emailNotificationSettings")
    public Object emailNotificationSettings;

    @JsonProperty("verifiedEmail")
    public Boolean verifiedEmail;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Boolean getProUser() {
        return isProUser;
    }

    public void setProUser(Boolean proUser) {
        isProUser = proUser;
    }

    public Pin getPin() {
        return pin;
    }

    public void setPin(Pin pin) {
        this.pin = pin;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getStats() {
        return stats;
    }

    public void setStats(Object stats) {
        this.stats = stats;
    }

    public Object getEmailNotificationSettings() {
        return emailNotificationSettings;
    }

    public void setEmailNotificationSettings(Object emailNotificationSettings) {
        this.emailNotificationSettings = emailNotificationSettings;
    }

    public Boolean getVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(Boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }
}
