package me.winterguardian.core.portal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleCommandDestination extends SerializableDestination
{
	private String command;
	private boolean stop;
	
	public ConsoleCommandDestination(String command, boolean stop)
	{
		this.command = command;
		this.stop = stop;
	}
	
	public ConsoleCommandDestination(String string)
	{
		this.command = string;
		if(this.stop = string.startsWith("!"))
			this.command.substring(1, string.length());
	}
	
	@Override
	public void goTo(Player p)
	{
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("@p", p.getName()));
	}
	
	@Override
	public boolean cancelEvent()
	{
		return stop;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ":" + (this.stop ? "!" : "") + this.command;
	}
	
	public static ConsoleCommandDestination fromString(String string)
	{
		try
		{
			return new ConsoleCommandDestination(string);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}