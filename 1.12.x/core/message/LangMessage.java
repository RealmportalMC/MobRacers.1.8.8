package me.winterguardian.core.message;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;


public abstract class LangMessage extends Message
{
	private String path;
	
	public LangMessage(String path)
	{
		super();
		this.path = path;
	}

	public LangMessage(String path, boolean prefix)
	{
		super(prefix);
		this.path = path;
	}
	
	public LangMessage(String path, boolean prefix, MessageType type)
	{
		super(prefix, type);
		this.path = path;
	}

	public LangMessage(String path, boolean prefix, String hoverEvent, String hoverEventContent, String clickEvent, String clickEventContent)
	{
		super(prefix, hoverEvent, hoverEventContent, clickEvent, clickEventContent);
		this.path = path;
	}

	public LangMessage(String path, int delay, int fadeIn, int fadeOut)
	{
		super(delay, fadeIn, fadeOut);
		this.path = path;
	}

	@Override
	public String getPrefix()
	{
		if(getLangConfig() == null)
			return "prefix> ";
		
		String s;
		return ChatColor.translateAlternateColorCodes('&', (s = getLangConfig().getString("prefix")) != null ? s : "prefix> ");
	}

	@Override
	protected String getContent()
	{
		if(getLangConfig() == null)
			return path;
		String s;
		return ChatColor.translateAlternateColorCodes('&', (s = getLangConfig().getString(path)) != null ? s : path);
	}
	
	protected abstract YamlConfiguration getLangConfig();
}
