package me.winterguardian.core.game;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public interface Game
{	
	boolean contains(Player p);
	
	void leave(Player p);
	void join(Player p);

	@Deprecated
	Location getWarp();
	@Deprecated
	String getChatPrefix(Player p);
	@Deprecated
	String getChatName(Player p);
	
	String getColoredName();
	String getStatus();
}
