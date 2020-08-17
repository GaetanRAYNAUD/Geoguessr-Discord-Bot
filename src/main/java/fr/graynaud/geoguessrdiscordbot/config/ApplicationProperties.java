package fr.graynaud.geoguessrdiscordbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    private String discordToken;

    private String geoguessrNcfa;

    public String getDiscordToken() {
        return discordToken;
    }

    public void setDiscordToken(String discordToken) {
        this.discordToken = discordToken;
    }

    public String getGeoguessrNcfa() {
        return geoguessrNcfa;
    }

    public void setGeoguessrNcfa(String geoguessrNcfa) {
        this.geoguessrNcfa = geoguessrNcfa;
    }
}
