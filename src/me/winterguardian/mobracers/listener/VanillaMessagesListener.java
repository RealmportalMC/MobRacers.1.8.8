package me.winterguardian.mobracers.listener;

import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.mobracers.MobRacersConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * Created by Alexander Winter on 2016-01-05.
 */
public class VanillaMessagesListener implements Listener
{
	private StateGame game;

	public VanillaMessagesListener(StateGame game)
	{
		this.game = game;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(!((MobRacersConfig) game.getConfig()).disableVanillaMessages())
			return;

		event.setJoinMessage(null);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		if(!((MobRacersConfig) game.getConfig()).disableVanillaMessages())
			return;

		event.setQuitMessage(null);
	}
}
