package me.winterguardian.core.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.game.Game;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.message.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class JoinGameSubCommand extends SubCommand
{
	private Game game;
	private Message invalidSender;
	
	public JoinGameSubCommand(Game game)
	{
		this(game, "joindre", Arrays.asList("rejoindre", "join", "enter", "entrer"), null, null, null);
	}
	
	public JoinGameSubCommand(Game game, String name, List<String> aliases, Permission permission, String permissionMessage, String usage)
	{
		this(game, name, aliases, permission, permissionMessage, usage, ErrorMessage.COMMAND_INVALID_SENDER);
	}
	
	public JoinGameSubCommand(Game game, String name, List<String> aliases, Permission permission, String permissionMessage, String usage, Message invalidSender)
	{
		super(name, aliases, permission, permissionMessage, usage);
		this.game = game;
		this.invalidSender = invalidSender;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			game.join((Player) sender);
			return true;
		}
		
		invalidSender.say(sender);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
