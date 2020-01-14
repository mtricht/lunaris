package dev.tricht.lunaris.listeners;

public interface GameListener {

    public void onEvent(GameEvent event);
    public boolean supports(GameEvent event);
}
