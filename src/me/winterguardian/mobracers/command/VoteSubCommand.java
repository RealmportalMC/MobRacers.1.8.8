package me.winterguardian.mobracers.command;

import me.winterguardian.core.command.SubCommand;
import me.winterguardian.core.json.JsonUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.state.lobby.ArenaSelectionState;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VoteSubCommand extends SubCommand 
{
	private MobRacersGame game;
	
	public VoteSubCommand(MobRacersGame game)
	{
		super("vote", Arrays.asList("choisir", "choose", "arenavote", "arena-vote", "votemap", "votearena"), MobRacersPlugin.VOTE, CourseMessage.COMMAND_INVALID_PERMISSION.toString(), "§c"+ CourseMessage.COMMAND_USAGE + ": §f/mobracers vote <arena>");
		this.game = game;
	}

	@Override
	public boolean onSubCommand(CommandSender sender, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			CourseMessage.COMMAND_INVALID_SENDER.say(sender);
			return true;
		}
		
		if(!game.contains((Player) sender))
		{
			CourseMessage.COMMAND_VOTE_CANTOUTOFGAME.say(sender);
			return true;
		}
		
		if(!(game.getState() instanceof ArenaSelectionState))
		{
			CourseMessage.COMMAND_VOTE_CANTVOTENOW.say(sender);
			return true;
		}
	
		if(args.length == 0)
		{
			CourseMessage.COMMAND_VOTE_LIST.say(sender);
			String arenas = "[";
			for(Arena arena : Arena.getReadyArenaList())
				arenas += JsonUtil.toJson("§e" + arena.getName() + ", ",
						"show_text", JsonUtil.toJson(CourseMessage.COMMAND_VOTE_HOVER.toString("<arena>", arena.getName(), "<author>", arena.getAuthor(), "<laps>", arena.getLaps() + "")),
						"run_command", "\"/mobracers vote " + arena.getName() + "\"") + ",";
			JsonUtil.sendJsonMessage((Player)sender, arenas.substring(0, arenas.length() - 1) + "]");
			return true;
		}
		
		return ((ArenaSelectionState)game.getState()).vote((Player) sender, args[0]);
	}

	@Override
	public List<String> onSubTabComplete(CommandSender sender, String label, String[] args)
	{
		if(args.length == 1)
		{
			List<String> arenas = new ArrayList<>();
			for(Arena arena : Arena.getReadyArenaList())
				arenas.add(arena.getName());
			return arenas;
		}
		return null;
	}

}
