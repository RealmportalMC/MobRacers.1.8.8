package me.winterguardian.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

/**
 * Insanely simple listener to change dynamically the MOTD of the server
 *
 * Created by Alexander Winter on 2015-11-29.
 */
public abstract class ServerMOTDListener implements Listener
{
    public abstract String getMOTD();

    @EventHandler(ignoreCancelled = true)
    public void onPing(ServerListPingEvent event)
    {
        if(!isEnabled())
            return;

        event.setMotd(getMOTD());
    }

    public boolean isEnabled()
    {
        return true;
    }
}
