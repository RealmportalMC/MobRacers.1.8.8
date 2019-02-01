package me.winterguardian.core.listener;

import me.winterguardian.core.game.Game;
import me.winterguardian.core.message.Message;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;

public abstract class GameAutoJoinListener implements Listener
{
	private Game game;
	
	public GameAutoJoinListener(Game game)
	{
		this.game = game;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		if(!this.isEnabled())
			return;
		
		if(getNoAutoJoinPermission() != null && event.getPlayer().hasPermission(getNoAutoJoinPermission()))
			return;
		
		this.game.join(event.getPlayer());
		
		if(this.game.contains(event.getPlayer()))
			return;
		
		if(getNoKickPermission() != null && event.getPlayer().hasPermission(getNoKickPermission()))
			return;
		
		getKickMessage().say(event.getPlayer());
	}
	
	public abstract boolean isEnabled();
	public abstract Permission getNoAutoJoinPermission();
	public abstract Permission getNoKickPermission();
	public abstract Message getKickMessage();
}
