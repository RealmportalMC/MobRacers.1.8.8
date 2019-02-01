package me.winterguardian.core.command;

import java.util.List;

import me.winterguardian.core.message.HardcodedMessage;
import me.winterguardian.core.message.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

/**
 * 
 * @author Alexander Winter
 */
public abstract class SubCommand
{
	private String name;
	private Message permissionMessage, usage;
	private List<String> aliases;
	private Permission permission;
	
	public SubCommand(String name, List<String> aliases, Permission permission, String permissionMessage, String usage)
	{
		this.name = name;
		this.aliases = aliases;
		this.permission = permission;
		this.permissionMessage = new HardcodedMessage(permissionMessage, false);
		this.usage = new HardcodedMessage(usage, false);
	}
	
	public SubCommand(String name, List<String> aliases, Permission permission, Message permissionMessage, Message usage)
	{
		this.name = name;
		this.aliases = aliases;
		this.permission = permission;
		this.permissionMessage = permissionMessage;
		this.usage = usage;
	}
	
	
	public abstract boolean onSubCommand(CommandSender sender, String label, String[] args);
	public abstract List<String> onSubTabComplete(CommandSender sender, String label, String[] args);

	public boolean doesExecute(String label)
	{
		if(this.name.equalsIgnoreCase(label))
			return true;
		if(this.aliases != null)
			for(String alias : this.aliases)
				if(alias.equalsIgnoreCase(label))
					return true;
		return false;
	}
	

	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public Message getPermissionMessage()
	{
		return permissionMessage;
	}


	public void setPermissionMessage(Message permissionMessage)
	{
		this.permissionMessage = permissionMessage;
	}


	public List<String> getAliases()
	{
		return aliases;
	}


	public void setAliases(List<String> aliases)
	{
		this.aliases = aliases;
	}


	public Permission getPermission()
	{
		return permission;
	}


	public void setPermission(Permission permission)
	{
		this.permission = permission;
	}


	public Message getUsage()
	{
		return usage;
	}


	public void setUsage(Message usage)
	{
		this.usage = usage;
	}	
}
