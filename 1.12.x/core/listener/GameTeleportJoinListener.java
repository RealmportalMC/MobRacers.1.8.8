package me.winterguardian.core.listener;


import me.winterguardian.core.game.Game;
import me.winterguardian.core.world.Region;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.Permission;

public abstract class GameTeleportJoinListener implements Listener
{
	private Game game;
	
	public GameTeleportJoinListener(Game game)
	{
		this.game = game;
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerTeleportIn(PlayerTeleportEvent event)
	{
		if(!this.isEnabled() || getRegion() == null)
			return;
		
		if(event.getPlayer().hasPermission(getNoTeleportJoinPermission()))
			return;
		
		if(getRegion() == null)
			return;
		
		if(game.contains(event.getPlayer()))
			return;
		
		if(getRegion().contains(event.getTo()) && !getRegion().contains(event.getFrom()))
		{
			event.setCancelled(true);
			game.join(event.getPlayer());
		}
	}
	

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPlayerTeleportOut(PlayerTeleportEvent event)
	{
		if(!this.isEnabled() || getRegion() == null)
			return;
		
		if(event.getPlayer().hasPermission(getNoTeleportJoinPermission()))
			return;
		
		if(getRegion() == null)
			return;
		
		if(!game.contains(event.getPlayer()))
			return;
		
		if(getRegion().contains(event.getFrom()) && !getRegion().contains(event.getTo()))
		{
			event.setCancelled(true);
			game.leave(event.getPlayer());
		}
	}
	
	public abstract boolean isEnabled();
	public abstract Region getRegion();
	public abstract Permission getNoTeleportJoinPermission(); 
}
