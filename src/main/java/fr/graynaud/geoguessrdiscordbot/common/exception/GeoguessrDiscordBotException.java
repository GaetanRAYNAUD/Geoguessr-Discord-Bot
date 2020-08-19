package fr.graynaud.geoguessrdiscordbot.common.exception;

public class GeoguessrDiscordBotException extends RuntimeException {

    public GeoguessrDiscordBotException() {
    }

    public GeoguessrDiscordBotException(String message) {
        super(message);
    }

    public GeoguessrDiscordBotException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeoguessrDiscordBotException(Throwable cause) {
        super(cause);
    }

    public GeoguessrDiscordBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
