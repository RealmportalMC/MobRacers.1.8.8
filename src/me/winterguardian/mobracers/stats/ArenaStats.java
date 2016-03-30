package me.winterguardian.mobracers.stats;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import me.winterguardian.core.Core;
import me.winterguardian.mobracers.vehicle.Vehicle;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ArenaStats
{
	private static boolean ready = false;
	private static HashMap<String, ArenaStats> arenaStats;
	
	private HashMap<String, PlayerArenaStats> players;
	private HashMap<Integer, String> top;
	
	private ArenaStats()
	{
		this.players = new HashMap<>();
		this.top = new HashMap<>();
	}
	
	public String getPlayer(int position)
	{
		return this.top.get(position);
	}
	
	public int getPosition(String player)
	{
		for(Integer i : top.keySet())
			if(top.get(i).equalsIgnoreCase(player))
				return i;
		return -1;
	}
	
	public int getSize()
	{
		return top.size();
	}
	
	public PlayerArenaStats getPlayerStats(String player)
	{
		return this.players.get(player);
	}

	public static void init()
	{
		new Thread()
		{
			@Override
			public void run()
			{
				if(!Core.getUserDatasManager().isEnabled())
					return;

				arenaStats = new HashMap<>();
				
				for(UUID id : Core.getUserDatasManager().getLoader().listUsers())
				{
					OfflinePlayer player = Bukkit.getOfflinePlayer(id);
					CourseStats stats = new CourseStats(player, Core.getUserDatasManager().getUserData(player));
					if(stats.getPlayedArenas() == null)
						continue;
					
					for(String arena : stats.getPlayedArenas())
					{
						if(!arenaStats.containsKey(arena))
							arenaStats.put(arena, new ArenaStats());
						arenaStats.get(arena).updateTime(stats.getName(), stats.getBestTime(arena), stats.getBestVehicle(arena));
					}
				}
				for(String arena : arenaStats.keySet())
					arenaStats.get(arena).updateTop();
				ready = true;
			}
		}.start();
	}
	
	public static void update(String arena, Player player, long time, Vehicle vehicle)
	{
		if(!arenaStats.containsKey(arena))
			arenaStats.put(arena, new ArenaStats());
		
		arenaStats.get(arena).updateTime(player.getName(), time, vehicle.getType().name());
		arenaStats.get(arena).updateTop();
	}
	
	private void updateTime(String player, long time, String vehicle)
	{
		if(getPlayerStats(player) == null || getPlayerStats(player).getTime() > time)	
			players.put(player, new PlayerArenaStats(time, vehicle));
	}
	
	public void updateTop()
	{
		this.top.clear();
		
		for(int i = 0; i < players.size(); i++)
		{
			for(String player : players.keySet())
			{
				if(this.top.containsValue(player))
					continue;
				
				String current = this.top.get(i);
				
				if(current == null || getPlayerStats(current).getTime() > getPlayerStats(player).getTime() || (getPlayerStats(current).getTime() == getPlayerStats(player).getTime() && player.compareToIgnoreCase(current) < 0))
					this.top.put(i, player);
					
			}
		}
	}
	
	public static boolean isReady()
	{
		return ready;
	}
	
	public static ArenaStats getStats(String arenaName)
	{
		if(!isReady())
			return null;
		
		return arenaStats.get(arenaName);
	}
	
	public static Set<String> getArenaNames()
	{
		if(!isReady())
			return null;
		
		return arenaStats.keySet();
	}
	
	public static class PlayerArenaStats
	{
		private long time;
		private String vehicle;
		
		public PlayerArenaStats(long time, String vehicle)
		{
			this.time = time;
			this.vehicle = vehicle;
		}

		public long getTime()
		{
			return time;
		}

		public void setTime(long time)
		{
			this.time = time;
		}

		public String getVehicle()
		{
			return vehicle;
		}

		public void setVehicle(String vehicle)
		{
			this.vehicle = vehicle;
		}
	}
}
