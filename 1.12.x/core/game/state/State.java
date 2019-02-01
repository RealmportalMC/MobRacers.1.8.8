package me.winterguardian.core.game.state;

import org.bukkit.entity.Player;

public interface State
{
	void join(Player p);
	void leave(Player p);
	void start();
	void end();
	String getStatus();
}
