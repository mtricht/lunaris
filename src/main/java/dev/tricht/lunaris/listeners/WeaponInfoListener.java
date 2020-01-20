package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.data.WeaponInfo;

public class WeaponInfoListener implements GameListener {

    public WeaponInfoListener() { }

    @Override
    public void onEvent(GameEvent event) {

    }

    @Override
    public boolean supports(GameEvent event) {
        return false;
    }
}
