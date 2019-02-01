package me.winterguardian.core.portal;

import org.bukkit.entity.Player;

public interface PortalDestination
{
	void goTo(Player p);
	boolean cancelEvent();
}