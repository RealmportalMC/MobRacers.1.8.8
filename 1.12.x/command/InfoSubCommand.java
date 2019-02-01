package me.winterguardian.mobracers.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.MobRacersPlugin;

import org.bukkit.command.CommandSender;

public class InfoSubCommand extends SubCommand
{
	private MobRacersGame game;
	
	public InfoSubCommand(MobRacersGame game)
	{
		super("info", Arrays.asList("infos", "aide", "help", "?"), null, null, "§c"+ CourseMessage.COMMAND_USAGE + ": §f/mobracers info");
		this.game = game;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		CourseMessage.COMMAND_INFO_TITLE.say(sender);
		if(!game.getConfig().isAutoJoin())
		{
			CourseMessage.COMMAND_INFO_COMMAND_JOIN.say(sender);
			CourseMessage.COMMAND_INFO_COMMAND_LEAVE.say(sender);
		}
		CourseMessage.COMMAND_INFO_COMMAND_VOTE.say(sender);
		if(game.getConfig().enableStats())
		{
			CourseMessage.COMMAND_INFO_COMMAND_STATS.say(sender);
			CourseMessage.COMMAND_INFO_COMMAND_ACHIEVEMENTS.say(sender);
			CourseMessage.COMMAND_INFO_COMMAND_RANKING.say(sender);
			CourseMessage.COMMAND_INFO_COMMAND_BUY.say(sender);
		}
		if(sender.hasPermission(MobRacersPlugin.STAFF))
		{
			CourseMessage.COMMAND_INFO_COMMAND_ARENA.say(sender);
			CourseMessage.COMMAND_INFO_COMMAND_CONFIG.say(sender);
		}
		
		CourseMessage.COMMAND_INFO_COMMAND_VERSION.say(sender);
		
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
