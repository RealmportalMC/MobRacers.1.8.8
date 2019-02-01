package me.winterguardian.core.message;

import java.util.Collection;
import java.util.regex.Pattern;

import me.winterguardian.core.game.Game;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.util.ActionBarUtil;
import me.winterguardian.core.util.TitleUtil;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public abstract class Message
{
	public static final Message NULL = new Message(false, MessageType.NULL)
	{
		@Override protected String getPrefix() {return null;}
		@Override protected String getContent() {return null;}
	};
	
	private boolean hasPrefix;
	private MessageType type;
	
	private int delay, fadeIn, fadeOut;
	
	private String hoverEvent;
	private String hoverEventContent;
	private String clickEvent;
	private String clickEventContent;
	
	protected Message()
	{
		this(true);
	}
	
	protected Message(boolean prefix)
	{
		this(prefix, MessageType.CHAT_NORMAL);
	}
	
	protected Message(boolean prefix, MessageType type)
	{
		this.hasPrefix = prefix;
		this.type = type;
	}
	
	protected Message(int delay, int fadeIn, int fadeOut)
	{
		this.hasPrefix = false;
		this.type = MessageType.TITLE;
		this.delay = delay;
		this.fadeIn = fadeIn;
		this.fadeOut = fadeOut;
	}
	
	protected Message(boolean prefix, String hoverEvent, String hoverEventContent, String clickEvent, String clickEventContent)
	{
		this(prefix, MessageType.CHAT_JSON);
		
		this.hoverEvent = hoverEvent;
		this.hoverEventContent = hoverEventContent;
		this.clickEvent = clickEvent;
		this.clickEventContent = clickEventContent;
	}
	
	public boolean hasPrefix()
	{
		return hasPrefix;
	}

	public MessageType getType()
	{
		return type;
	}


	public enum MessageType
	{
		CHAT_NORMAL(),
		CHAT_JSON(),
		ACTIONBAR(),
		TITLE(),
		KICK(),
		NULL(),
		;
	}
	
	@Override
	public String toString()
	{
		return this.hasPrefix() ? getPrefix() + "§r" + getContent() : getContent();
	}
	
	public String toString(String... replacements)
	{
		if(getType() == MessageType.NULL || getContent() == null || getContent().equals("null"))
			return "null";
		
		String message = getContent();
		
		if(replacements != null)
			for(int i = 0; i + 1 < replacements.length; i += 2)
				message = message.replace(replacements[i], replacements[i + 1]);
		
		if(this.hasPrefix())
			message = getPrefix() + "§r" + message;
		return message;
	}

	public void say(CommandSender receiver, String... replacements)
	{
		if(getType() == MessageType.NULL || getContent() == null || getContent().equals("null") || receiver == null)
			return;
		
		String message = getContent();
		
		if(replacements != null)
			for(int i = 0; i + 1 < replacements.length; i += 2)
				message = message.replace(replacements[i], replacements[i + 1]);
		
		String sub = null;
		if(getType() == MessageType.TITLE)
		{
			sub = message.split("\\n")[1];
			message = message.split("\\n")[0];
		}
		
		if(this.hasPrefix())
			message = getPrefix() + "§r" + message;
		
		switch(getType())
		{
		case ACTIONBAR:
			if(receiver instanceof Player)
			{
				ActionBarUtil.sendActionMessage((Player)receiver, message);
				return;
			}
		case TITLE:
			if(receiver instanceof Player)
			{
				TitleUtil.displayTitle((Player)receiver, message, sub, fadeIn, delay, fadeOut);
				return;
			}
		case CHAT_JSON:
			if(receiver instanceof Player)
			{
				JsonUtil.sendJsonMessage((Player)receiver, JsonUtil.toJson(message, this.hoverEvent, this.hoverEventContent, this.clickEvent, this.clickEventContent));
				return;
			}
		case KICK:
			if(receiver instanceof Player)
			{
				receiver.sendMessage(message);
				return;
			}
		default:
			for(String string : message.split(Pattern.quote("\\n")))
				receiver.sendMessage(string);

		}
	}
	
	public void sayIfOnline(OfflinePlayer receiver, String... replacements)
	{
		if(receiver != null && receiver.isOnline())
			this.say(receiver.getPlayer(), replacements);
	}
	
	public void say(Collection<? extends CommandSender> receivers, String... replacements)
	{
		for(CommandSender receiver : receivers)
			this.say(receiver, replacements);
			
	}
	
	public void sayMembers(Game game, String... replacements)
	{
		if(game == null)
			return;
		
		for(Player p : Bukkit.getOnlinePlayers())
			if(game.contains(p))
				this.say(p, replacements);
			
	}
	
	public void sayMembers(World world, String... replacements)
	{
		if(world == null)
			return;
		
		for(Player p : world.getPlayers())
			this.say(p, replacements);
	}
	
	public void sayAll(String... replacements)
	{
		for(Player p : Bukkit.getOnlinePlayers())
			this.say(p, replacements);
		this.say(Bukkit.getConsoleSender(), replacements);
	}
	
	public void sayPermissibles(Permission permission, String... replacements)
	{
		for(Player p : Bukkit.getOnlinePlayers())
			if(p.hasPermission(permission))
				this.say(p, replacements);
		this.say(Bukkit.getConsoleSender(), replacements);
	}
	
	protected abstract String getPrefix();
	protected abstract String getContent();
}
