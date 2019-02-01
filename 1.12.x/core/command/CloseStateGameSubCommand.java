package me.winterguardian.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.message.ErrorMessage;
import me.winterguardian.core.message.Message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class CloseStateGameSubCommand extends SubCommand
{
	private StateGame game;
	private Message closeMessage;
	
	public CloseStateGameSubCommand(StateGame game, Permission permission)
	{
		this(game, permission, Message.NULL);
	}
	
	public CloseStateGameSubCommand(StateGame game, String name, List<String> aliases, Permission permission, String permissionMessage, String usage)
	{
		this(game, name, aliases, permission, permissionMessage, usage, Message.NULL);
	}
	
	public CloseStateGameSubCommand(StateGame game, Permission permission, Message closeMessage)
	{
		this(game, "fermer", Arrays.asList("fermeture", "close", "shut", "shutdown", "end"), permission, ErrorMessage.COMMAND_INVALID_PERMISSION.toString(), null);
		this.closeMessage = closeMessage;
	}
	
	public CloseStateGameSubCommand(StateGame game, String name, List<String> aliases, Permission permission, String permissionMessage, String usage, Message closeMessage)
	{
		super(name, aliases, permission, permissionMessage, usage);
		this.game = game;
		this.closeMessage = closeMessage;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		this.closeMessage.say(game.getPlayers(), "<closer>", sender.getName());
		for(Player p : new ArrayList<>(game.getPlayers()))
			game.leave(p);
		
		game.setOpen(false);
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
