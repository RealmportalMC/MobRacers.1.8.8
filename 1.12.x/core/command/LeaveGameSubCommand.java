package me.winterguardian.core.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.game.Game;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.message.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class LeaveGameSubCommand extends SubCommand
{
	private Game game;
	private Message invalidSender;
	
	public LeaveGameSubCommand(Game game)
	{
		this(game, "quitter", Arrays.asList("partir", "leave", "quit", "sortir", "exit"), null, null, null);
	}
	
	public LeaveGameSubCommand(Game game, String name, List<String> aliases, Permission permission, String permissionMessage, String usage)
	{
		this(game, name, aliases, permission, permissionMessage, usage, ErrorMessage.COMMAND_INVALID_SENDER);
	}
	
	public LeaveGameSubCommand(Game game, String name, List<String> aliases, Permission permission, String permissionMessage, String usage, Message invalidSender)
	{
		super(name, aliases, permission, permissionMessage, usage);
		this.game = game;
		this.invalidSender = invalidSender;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			invalidSender.say(sender);
			return true;
		}
		
		if(game instanceof StateGame && ((StateGame)game).getConfig() != null && ((StateGame)game).getConfig().isAutoJoin() && !sender.hasPermission(((StateGame) game).getNoAutoJoinPermission()))
		{
			if(((StateGame)game).getCantLeaveInAutoJoinMessage() != null)
				((StateGame)game).getCantLeaveInAutoJoinMessage().say(sender);
			return true;
		}
		
		game.leave((Player) sender);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}