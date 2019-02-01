package me.winterguardian.core.listener;

import java.util.Collection;

import me.winterguardian.core.message.Message;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permission;

public abstract class CommandBlockerListener implements Listener
{
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
	{	if(!this.isEnabled())
			return;
		
		if(getCommandBlockingBypassPermission() != null && event.getPlayer().hasPermission(getCommandBlockingBypassPermission()))
			return;
		
		if(this.isAffected(event.getPlayer()))
		{	String msg = null;
			if (event.getMessage().contains(" "))
			{	String[] ss = event.getMessage().split(" ");
				msg = ss[0].replaceFirst("/", "");
			}
			else
				msg = event.getMessage().replaceFirst("/", "");
			
			boolean allow = false;
			
			for(String string : getAllowedCommands())
				if(msg.equalsIgnoreCase(string))
				{	allow = true;
					return;
				}
			
			if(!allow)
			{	getCommandBlockedMessage().say(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}
	
	public abstract boolean isEnabled();
	public abstract Permission getCommandBlockingBypassPermission();
	public abstract boolean isAffected(Player p);
	public abstract Collection<String> getAllowedCommands();
	public abstract Message getCommandBlockedMessage();
}
