package me.winterguardian.core.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.message.Message;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;

public abstract class CommandSplitter extends AutoRegistrationCommand
{
	private Set<SubCommand> subCommands;
	private Message invalidSubCommand;
	
	public CommandSplitter()
	{
		this(ErrorMessage.COMMAND_INVALID_SUBCOMMAND);
	}
	
	public CommandSplitter(Message invalidSubCommand)
	{
		this.subCommands = new HashSet<SubCommand>();
		this.invalidSubCommand = invalidSubCommand;
	}
	
	@Override
	public String getUsage()
	{
		String subCommands = "args";
		
		if(this.subCommands.size() > 0)
			for(SubCommand subCommand : this.subCommands)
				if(subCommands == "args")
					subCommands = subCommand.getName();
				else
					subCommands += "|" + subCommand.getName();
		
		return "/" + this.getName() + " <" + subCommands + ">";
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length <= 0)
			return this.index(sender, cmd, label);
		
		for(SubCommand sub : this.subCommands)
			if(sub.doesExecute(args[0]))
				if(sub.getPermission() == null || sender.hasPermission(sub.getPermission()))
				{
					String[] subArgs = new String[args.length - 1];
					for(int i = 1; i < args.length; i++)
						subArgs[i - 1] = args[i];
					if(!sub.onSubCommand(sender, args[0], subArgs))
						if(sub.getUsage() != null)
							sub.getUsage().say(sender, "<command>", sub.getName());
					return true;
				}
				else
				{
					sub.getPermissionMessage().say(sender, "<permission>", sub.getPermission().getName());
					return true;
				}
		invalidSubCommand.say(sender);
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(args.length <= 1)
		{
			List<String> completions = new ArrayList<String>();
			for(SubCommand sub : this.getSubCommands())
				if(sub.getPermission() == null || sender.hasPermission(sub.getPermission()))
					if(sub.getName().startsWith(args[0]))
						completions.add(sub.getName());
			return completions;
		}
		
		for(SubCommand sub : this.subCommands)
			if(sub.doesExecute(args[0]))
				if(sub.getPermission() == null || sender.hasPermission(sub.getPermission()))
				{
					String[] subArgs = new String[args.length - 1];
					for(int i = 1; i < args.length; i++)
						subArgs[i - 1] = args[i];
					return sub.onSubTabComplete(sender, args[0], subArgs);
				}
		return null;
	}
	
	public abstract boolean index(CommandSender sender, Command cmd, String label);
	
	public Set<SubCommand> getSubCommands()
	{
		return this.subCommands;
	}
	
	@Override
	public Collection<Permission> getOtherPermissions()
	{
		List<Permission> perms = new ArrayList<Permission>();
		
		for(SubCommand sub : this.getSubCommands())
			perms.add(sub.getPermission());
		return perms;
	}
}
