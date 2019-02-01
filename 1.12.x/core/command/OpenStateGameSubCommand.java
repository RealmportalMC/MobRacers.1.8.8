package me.winterguardian.core.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.message.Message;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class OpenStateGameSubCommand extends SubCommand
{
	private StateGame game;
	private Message openMessage;
	
	public OpenStateGameSubCommand(StateGame game, Permission permission)
	{
		this(game, "ouvrir", Arrays.asList("ouverture", "open"), permission, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), null);
	}
	
	public OpenStateGameSubCommand(StateGame game, String name, List<String> aliases, Permission permission, String permissionMessage, String usage)
	{
		this(game, name, aliases, permission, permissionMessage, usage, Message.NULL);
	}
	
	public OpenStateGameSubCommand(StateGame game, Permission permission, Message openMessage)
	{
		this(game, "ouvrir", Arrays.asList("ouverture", "open"), permission, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), null, openMessage);
	}
	
	public OpenStateGameSubCommand(StateGame game, String name, List<String> aliases, Permission permission, String permissionMessage, String usage, Message openMessage)
	{
		super(name, aliases, permission, permissionMessage, usage);
		this.game = game;
		this.openMessage = openMessage;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		openMessage.say(getRecipients(), "<opener>", sender.getName());
		game.setOpen(true);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}
	
	protected Collection<? extends Player> getRecipients()
	{
		return Bukkit.getOnlinePlayers();
	}
}
