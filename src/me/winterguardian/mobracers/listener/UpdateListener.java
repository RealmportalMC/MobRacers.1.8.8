package me.winterguardian.mobracers.listener;

import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.command.VersionSubCommand;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListener implements Listener
{
	private StateGame game;
	
	public UpdateListener(StateGame game)
	{
		this.game = game;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(!((MobRacersConfig) game.getConfig()).sendUpdateNotifications())
			return;
				
		if(!event.getPlayer().hasPermission(MobRacersPlugin.ADMIN))
			return;
		
		if(VersionSubCommand.isLatest(VersionSubCommand.getLastVersionFetch(), game.getPlugin()))
			return;
		
		CourseMessage.UPDATE_NOTIFICATION.say(event.getPlayer());
	}
}
