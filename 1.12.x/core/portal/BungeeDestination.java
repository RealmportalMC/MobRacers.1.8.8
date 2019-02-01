package me.winterguardian.core.portal;

import me.winterguardian.core.Core;

import org.bukkit.entity.Player;

public class BungeeDestination extends SerializableDestination
{
	private String server;
	
	public BungeeDestination(String server)
	{
		this.server = server;
	}
	
	@Override
	public void goTo(Player p)
	{
		Core.getBungeeMessager().sendToServer(p, server);
	}
	
	@Override
	public boolean cancelEvent()
	{
		return true;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ":" + this.server;
	}
	
	public static BungeeDestination fromString(String string)
	{
		try
		{
			return new BungeeDestination(string);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}