package me.winterguardian.core.portal;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public abstract class AutoRegistrationPortal implements Portal, Listener
{
	public void register(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	public void unregister()
	{
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		if(getRegion() == null || !getRegion().contains(event.getPlayer().getLocation()))
			return;
		
		getDestination().goTo(event.getPlayer());
		event.setCancelled(getDestination().cancelEvent());
	}
}