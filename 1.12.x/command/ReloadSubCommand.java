package me.winterguardian.mobracers.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.game.state.StateGame;
import me.winterguardian.core.message.Message;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class ReloadSubCommand extends SubCommand
{
	private StateGame game;
	
	public ReloadSubCommand(StateGame game)
	{
		super("reload", Arrays.asList("rel", "rl", "r", "refresh"), MobRacersPlugin.ADMIN, CourseMessage.COMMAND_INVALID_PERMISSION, Message.NULL);
		this.game = game;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		try
		{
			Plugin plugin = game.getPlugin();
			plugin.onDisable();
			plugin.onEnable();
			CourseMessage.COMMAND_RELOAD_DONE.say(sender);
		}
		catch(Exception e)
		{
			CourseMessage.COMMAND_RELOAD_ERROR.say(sender);
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
