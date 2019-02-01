package me.winterguardian.core.portal;

import org.bukkit.entity.Player;

public class CommandDestination extends SerializableDestination
{
	private String command;
	private boolean stop;
	
	public CommandDestination(String command, boolean stop)
	{
		this.command = command;
		this.stop = stop;
	}
	
	public CommandDestination(String string)
	{
		this.command = string;
		if(this.stop = string.startsWith("!"))
			this.command.substring(1, string.length());
	}
	
	@Override
	public void goTo(Player p)
	{
		if(this.command.startsWith("/"))
		{
			p.performCommand(this.command.substring(1));
			return;
		}
		
		p.chat(this.command);
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
	
	public static CommandDestination fromString(String string)
	{
		try
		{
			return new CommandDestination(string);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}