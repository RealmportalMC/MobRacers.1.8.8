package me.winterguardian.core.message;


import org.bukkit.ChatColor;

public class HardcodedMessage extends Message
{
	private String content;
	
	public HardcodedMessage(String content)
	{
		super();
		this.content = content;
	}

	public HardcodedMessage(String content, boolean prefix)
	{
		super(prefix);
		this.content = content;
	}
	
	public HardcodedMessage(String content, boolean prefix, MessageType type)
	{
		super(prefix, type);
		this.content = content;
	}

	public HardcodedMessage(String content, boolean prefix, String hoverEvent, String hoverEventContent, String clickEvent, String clickEventContent)
	{
		super(prefix, hoverEvent, hoverEventContent, clickEvent, clickEventContent);
		this.content = content;
	}

	public HardcodedMessage(String title, String subTitle, int delay, int fadeIn, int fadeOut)
	{
		super(delay, fadeIn, fadeOut);
		this.content = title + "\n" + subTitle;
	}

	@Override
	protected String getPrefix()
	{
		return "§f§lSekai§6§lMC §f§l>§7 ";
	}

	@Override
	protected String getContent()
	{
		return ChatColor.translateAlternateColorCodes('&', this.content);
	}

	
}
