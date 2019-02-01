package me.winterguardian.mobracers.command;

import java.util.Arrays;
import java.util.List;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.stats.CourseAchievement;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AchievementSubCommand extends SubCommand
{
	public AchievementSubCommand()
	{
		super("achievement", Arrays.asList("succès", "success"), null, null, "§c"+ CourseMessage.COMMAND_USAGE + ": §f/mobracers achievement [done]");
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			CourseMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(args.length > 0 && (args[0].equalsIgnoreCase("done") || args[0].equalsIgnoreCase("terminé") || args[0].equalsIgnoreCase("terminés") || args[0].equalsIgnoreCase("finished") || args[0].equalsIgnoreCase("accomplis")))
		{
			if(CourseAchievement.getDone((Player) sender) == 0)
			{
				CourseMessage.COMMAND_ACHIEVEMENT_NOTHINGDONE.say(sender);
				return true;
			}
			
			CourseMessage.COMMAND_ACHIEVEMENT_LIST_DONE.say(sender);
			for(CourseAchievement achiev : CourseAchievement.values())
				if(achiev.isComplete((Player) sender))
					JsonUtil.sendJsonMessage((Player) sender, JsonUtil.toJson("  " + achiev.getName(), "show_text" , JsonUtil.toJson(achiev.getDescription()), null, null));
			return true;
		}
		
		if(CourseAchievement.getRemaining((Player) sender) == 0)
		{
			CourseMessage.COMMAND_ACHIEVEMENT_ALLDONE.say(sender);
			return true;
		}
		
		CourseMessage.COMMAND_ACHIEVEMENT_LIST_TODO.say(sender);
		for(CourseAchievement achiev : CourseAchievement.getTodo((Player) sender))
			JsonUtil.sendJsonMessage((Player) sender, JsonUtil.toJson("  " + achiev.getName() + " §r" + achiev.getProgression((Player) sender), "show_text" , JsonUtil.toJson(achiev.getDescription()), null, null));
		CourseMessage.COMMAND_ACHIEVEMENT_LIST_OTHERS.say(sender);
		
		return true;
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		return null;
	}

}
