package me.winterguardian.mobracers.command;

import java.util.Arrays;
import java.util.UUID;

import me.winterguardian.core.command.PlayerStatsAsyncSubCommand;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.stats.CourseStats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class StatsSubCommand extends PlayerStatsAsyncSubCommand
{
	public StatsSubCommand(Plugin plugin)
	{
		super(plugin, "statistics", Arrays.asList("stats", "statistic", "statistique", "statistiques", "score", "skills"), null, null, "§c" + CourseMessage.COMMAND_USAGE + ": §f/mobracers statistics [player]", CourseMessage.COMMAND_INVALID_SENDER, CourseMessage.COMMAND_INVALID_PLAYER);
	}

	public void displayStats(CommandSender sender, UUID player, MappedData data)
	{
		CourseStats stats = new CourseStats(Bukkit.getOfflinePlayer(player), data);

		sender.sendMessage(CourseMessage.COMMAND_STATS_HEADER + " §f§l: §2§l" + stats.getName());
		if(sender instanceof Player)
			JsonUtil.sendJsonMessage((Player) sender, JsonUtil.toJson(CourseMessage.COMMAND_STATS_VICTORIES.toString() + " §r" + CourseMessage.toString(stats.getTotalVictories())));
		else
			sender.sendMessage(CourseMessage.COMMAND_STATS_VICTORIES.toString() + " §r" + CourseMessage.toString(stats.getTotalVictories()));
		sender.sendMessage(CourseMessage.COMMAND_STATS_GAMESPLAYED.toString() + " §r" + CourseMessage.toString(stats.getTotalGamesPlayed()));
		if(sender instanceof Player)
				JsonUtil.sendJsonMessage((Player) sender, JsonUtil.toJson(CourseMessage.COMMAND_STATS_BESTVEHICLE.toString() + " §r" + stats.getMostPlayed().createNewVehicle().getName(), "show_text", JsonUtil.toJson("(" + stats.getVehicleGamesPlayed(stats.getMostPlayed().name()) + " " + CourseMessage.COMMAND_STATS_BESTVEHICLE_GAMESPLAYED_HOVER.toString() + ")"), null, null));
		else
			sender.sendMessage(CourseMessage.COMMAND_STATS_BESTVEHICLE.toString() + " §r" + stats.getMostPlayed().createNewVehicle().getName());
		sender.sendMessage(CourseMessage.COMMAND_STATS_ITEMS.toString() + " §r" + CourseMessage.toString(stats.getTotalCollectedItems()));
		sender.sendMessage(CourseMessage.COMMAND_STATS_PASSINGS.toString() + " §r" + CourseMessage.toString(stats.getTotalPassings()));
		sender.sendMessage(CourseMessage.COMMAND_STATS_POINTS.toString() + " §r" + CourseMessage.toString(stats.getPoints()));
		sender.sendMessage(CourseMessage.COMMAND_STATS_SCORE.toString() + " §r" + CourseMessage.toString(stats.getScore()));
	}

}
