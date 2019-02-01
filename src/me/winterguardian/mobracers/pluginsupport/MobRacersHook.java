package me.winterguardian.mobracers.pluginsupport;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.winterguardian.core.util.MathUtil;
import me.winterguardian.mobracers.MobRacersConfig;
import me.winterguardian.mobracers.MobRacersGame;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.state.game.GamePlayerData;
import me.winterguardian.mobracers.state.game.GameState;
import me.winterguardian.mobracers.stats.ArenaStats;
import me.winterguardian.mobracers.stats.ArenaStats.PlayerArenaStats;
import me.winterguardian.mobracers.stats.CourseStats;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MobRacersHook extends PlaceholderHook
{
	private MobRacersGame game;
	
	public MobRacersHook(MobRacersGame game)
	{
		this.game = game;
	}
	
	public boolean register(Plugin plugin)
	{
		return PlaceholderAPI.registerPlaceholderHook(plugin, this);
	}
	
	public boolean unregister(Plugin plugin)
	{
		return PlaceholderAPI.unregisterPlaceholderHook(plugin);
	}
	
	@Override
	public String onPlaceholderRequest(Player player, String id)
	{
		if(id.equalsIgnoreCase("currentplayers"))
			return game.getPlayers().size() + "";
		
		if(id.equalsIgnoreCase("minplayers"))
			return game.getMinPlayers() + "";
		
		if(id.equalsIgnoreCase("maxplayers"))
			return game.getMaxPlayers() + "";
		
		if(id.equalsIgnoreCase("pointcoeff"))
			return game.getConfig().getPointCoefficient() + "";
		
		if(id.equalsIgnoreCase("pointcoeffpercent"))
			return MathUtil.round(game.getConfig().getPointCoefficient() * 100, 2) + "%";
		
		if(id.equalsIgnoreCase("status"))
			return game.getStatus();
		
		if(id.equalsIgnoreCase("arenas"))
			return Arena.getArenaList().size() + "";
		
		if(id.equalsIgnoreCase("ready_arenas"))
			return Arena.getReadyArenaList().size() + "";
		
		if(id.split("_").length == 3 && id.split("_")[0].equalsIgnoreCase("arena"))
		{
			Arena arena;
			if(id.split("_")[1].equalsIgnoreCase("current") && game.getState() instanceof GameState)
				arena = ((GameState) game.getState()).getArena();
			else
				arena = new Arena(id.split("_")[1]);
			
			if(!arena.exists())
				return "";
			arena.load();
			
			String argument = id.split("_")[2];
			
			if(argument.equalsIgnoreCase("name"))
				return arena.getName();
			
			if(argument.equalsIgnoreCase("author"))
				return arena.getAuthor();

			if(argument.equalsIgnoreCase("laps"))
				return arena.getLaps() + "";
			
			if(argument.equalsIgnoreCase("timelimit"))
				return arena.getRaceTimeLimit() + "";
			
			if(argument.equalsIgnoreCase("items"))
				return arena.getItems().size() + "";
			
			if(argument.equalsIgnoreCase("spawns"))
				return arena.getSpawnPoints().size() + "";
			
			if(argument.equalsIgnoreCase("spectators"))
				return arena.getSpectatorLocations().size() + "";
			
			if(argument.equalsIgnoreCase("jukeboxes"))
				return arena.getJukeboxes().size() + "";
			
			if(argument.equalsIgnoreCase("ready"))
				return arena.isReady() + "";
		}
		
		if(id.split("_").length == 5 && id.split("_")[0].equalsIgnoreCase("arena"))
		{
			Arena arena;
			if(id.split("_")[1].equalsIgnoreCase("current") && game.getState() instanceof GameState)
				arena = ((GameState) game.getState()).getArena();
			else
				arena = new Arena(id.split("_")[1]);
			
			if(!arena.exists())
				return "";
			arena.load();
			
			if(id.split("_")[2].equalsIgnoreCase("ranking"))
			{
				if(!game.getConfig().enableStats())
					return "";
				
				ArenaStats stats = ArenaStats.getStats(arena.getName());
				String playerName;
				
				try
				{
					if(id.split("_")[3].equalsIgnoreCase("self"))
						playerName = player.getName();
					else
						playerName = stats.getPlayer(Integer.parseInt(id.split("_")[3]));
				}
				catch(Exception e)
				{
					return "";
				}
				
				if(playerName == null)
					return "";
				
				String statsArgument = id.split("_")[4];
				
				if(statsArgument.equalsIgnoreCase("name"))
					return playerName;
							
				PlayerArenaStats pstats = stats.getPlayerStats(playerName);
				
				if(pstats == null)
					return "";
				
				if(statsArgument.equalsIgnoreCase("vehicle"))
					return VehicleType.valueOf(pstats.getVehicle()).getName();
				
				if(statsArgument.equalsIgnoreCase("time"))
					return CourseStats.timeToString(pstats.getTime());
				
				if(statsArgument.equalsIgnoreCase("position"))
					return stats.getPosition(playerName) + "";
			}
		}
		
		if(id.split("_").length == 2 && id.split("_")[0].equalsIgnoreCase("game"))
		{
			if(!(game.getState() instanceof GameState))
				return "";
			
			if(id.split("_")[1].equalsIgnoreCase("finishedracers"))
				return ((GameState) game.getState()).getFinishedPlayers() + "";
			
			if(id.split("_")[1].equalsIgnoreCase("totalracers"))
				return ((GameState) game.getState()).getPlayerDatas().size() + "";
			
		}
		
		if(id.split("_").length == 4 && id.split("_")[0].equalsIgnoreCase("game"))
		{
			if(!(game.getState() instanceof GameState))
				return "";
			
			GameState game = (GameState) this.game.getState();
			
			Player racer = null;
			
			if(id.split("_")[1].equalsIgnoreCase("player"))
			{
				if(id.split("_")[2].equalsIgnoreCase("self"))
					racer = player;
				else if(id.split("_")[2].equalsIgnoreCase("last"))
					racer = game.getPlayer(game.getPlayerDatas().size());
				else try
				{
					racer = game.getPlayer(Integer.parseInt(id.split("_")[2]));
				}
				catch(Exception e)
				{
					return "";
				}
			}
			
			if(racer == null)
				return "";
			
			String argument = id.split("_")[3];
			
			if(argument.equalsIgnoreCase("name"))
				return racer.getName();
			
			if(argument.equalsIgnoreCase("displayname"))
				return racer.getDisplayName();
			
			GamePlayerData playerData = game.getPlayerData(racer);
			
			if(argument.equalsIgnoreCase("currentlap"))
				return (playerData.getCurrentLap() + 1) + "";
			
			if(argument.equalsIgnoreCase("remaininglaps"))
				return (game.getArena().getLaps() - playerData.getCurrentLap()) + "";
			
			if(argument.equalsIgnoreCase("vehicle"))
				return playerData.getVehicle() != null ? playerData.getVehicle().getName() : null;
				
			if(argument.equalsIgnoreCase("item"))
				return playerData.getCurrentItem() != null ? playerData.getCurrentItem().getName() : null;
				
			if(argument.equalsIgnoreCase("time"))
				return playerData.getStringRaceTime();
		}
		return "";
	}

}
