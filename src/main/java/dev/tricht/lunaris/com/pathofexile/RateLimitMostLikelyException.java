package dev.tricht.lunaris.com.pathofexile;

public class RateLimitMostLikelyException extends RuntimeException {
    public RateLimitMostLikelyException(String message, Exception cause) {
        super(message);
    }
}
