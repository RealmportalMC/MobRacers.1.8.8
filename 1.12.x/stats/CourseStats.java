package me.winterguardian.mobracers.stats;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import me.winterguardian.core.Core;
import me.winterguardian.core.playerstats.MappedData;
import me.winterguardian.core.playerstats.PlayerStats;
import me.winterguardian.core.util.TextUtil;
import me.winterguardian.mobracers.CourseMessage;
import me.winterguardian.mobracers.MobRacersPlugin;
import me.winterguardian.mobracers.arena.Arena;
import me.winterguardian.mobracers.vehicle.Vehicle;
import me.winterguardian.mobracers.vehicle.VehicleType;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CourseStats extends PlayerStats
{
	private OfflinePlayer player;
	
	public CourseStats(OfflinePlayer player, MappedData data)
	{
		super(data);
		this.player = player;
	}
	
	public OfflinePlayer getPlayer()
	{
		return this.player;
	}
	
	public int getScore()
	{
		return this.getContent().getInt("mobracers.score");
	}
	
	public void setScore(int score)
	{
		this.getContent().set("mobracers.score", score);
	}
	
	public void addGamePoints(int points)
	{
		if(MobRacersPlugin.getVault() == null || !MobRacersPlugin.getVault().isEnabled())
			super.addPoints(points);
		else if(!MobRacersPlugin.getVault().getEconomy().depositPlayer(getPlayer(), points).transactionSuccess())
			Bukkit.getLogger().warning("[MobRacers] §cCould not add point for player " + getPlayer().getName() + " with Vault !");
		this.setScore(this.getScore() + points);
		CourseMessage.POINTS_ADD.sayIfOnline(this.getPlayer(), "#", "" + points);
	}
	
	public void removePoints(int points)
	{
		if(MobRacersPlugin.getVault() == null || !MobRacersPlugin.getVault().isEnabled())
		{
			super.setPoints(super.getPoints() - points);
			return;
		}
		
		MobRacersPlugin.getVault().getEconomy().withdrawPlayer(getPlayer(), points);
	}
	
	@Override
	public int getPoints()
	{
		if(MobRacersPlugin.getVault() == null || !MobRacersPlugin.getVault().isEnabled())
			return super.getPoints();
		
		return (int) MobRacersPlugin.getVault().getEconomy().getBalance(getPlayer());
	}

	public YamlConfiguration getArenaSection()
	{
		try
		{
			YamlConfiguration section = new YamlConfiguration();
			section.loadFromString(getContent().getString("mobracers.arena", ""));

			return section;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new YamlConfiguration();
		}
	}

	public void setArenaSection(YamlConfiguration section)
	{
		getContent().set("mobracers.arena", section.saveToString());
	}

	public YamlConfiguration getVehicleSection()
	{
		try
		{
			YamlConfiguration section = new YamlConfiguration();
			section.loadFromString(getContent().getString("mobracers.vehicle", ""));

			return section;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new YamlConfiguration();
		}
	}

	public void setVehicleSection(YamlConfiguration section)
	{
		getContent().set("mobracers.vehicle", section.saveToString());
	}

	public YamlConfiguration getPurchaseSection()
	{
		try
		{
			YamlConfiguration section = new YamlConfiguration();
			section.loadFromString(getContent().getString("mobracers.purchase", ""));

			return section;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new YamlConfiguration();
		}
	}

	public void setAchievementSection(YamlConfiguration section)
	{
		getContent().set("mobracers.achievement", section.saveToString());
	}

	public YamlConfiguration getAchievementSection()
	{
		try
		{
			YamlConfiguration section = new YamlConfiguration();
			section.loadFromString(getContent().getString("mobracers.achievement", ""));

			return section;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new YamlConfiguration();
		}
	}

	public void setPurchaseSection(YamlConfiguration section)
	{
		getContent().set("mobracers.purchase", section.saveToString());
	}

	public int getTotalVictories()
	{
		int victories = 0;
		for(String key : getPlayedArenas())
			victories += getArenaVictories(key);
		return victories;
	}
	
	public int getArenaVictories(String arena)
	{
		return getArenaSection().getInt(arena + ".victories");
	}
	
	public int getVehicleVictories(String vehicle)
	{
		return getVehicleSection().getInt(vehicle + ".victories");
	}
	
	public void setArenaVictories(int victories, String arena)
	{
		YamlConfiguration section = getArenaSection();
		section.set(arena + ".victories", victories);
		setArenaSection(section);
	}
	
	public void setVehicleVictories(int victories, String vehicle)
	{
		YamlConfiguration section = getVehicleSection();
		section.set(vehicle + ".victories", victories);
		setVehicleSection(section);
	}
	
	public int getTotalGamesPlayed()
	{
		int gamesPlayed = 0;
		for(String key : getPlayedArenas())
			gamesPlayed += getArenaGamesPlayed(key);
		return gamesPlayed;
	}
	
	public int getArenaGamesPlayed(String arena)
	{
		return getArenaSection().getInt(arena + ".gamesplayed");
	}
	
	public int getVehicleGamesPlayed(String vehicle)
	{
		return getVehicleSection().getInt(vehicle + ".gamesplayed");
	}
	
	public void setArenaGamesPlayed(int gamesPlayed, String arena)
	{
		YamlConfiguration section = getArenaSection();
		section.set(arena + ".gamesplayed", gamesPlayed);
		setArenaSection(section);
	}
	
	public void setVehicleGamesPlayed(int gamesPlayed, String vehicle)
	{
		YamlConfiguration section = getVehicleSection();
		section.set(vehicle + ".gamesplayed", gamesPlayed);
		setVehicleSection(section);
	}
	
	public long getBestTime(String arena)
	{
		long bestTime = Long.MAX_VALUE;

		if(!getArenaSection().isConfigurationSection(arena + ".besttime"))
			return -1;

		for(String vehicle : getArenaSection().getConfigurationSection(arena + ".besttime").getKeys(false))
			if(this.alreadyPlayed(arena, vehicle) && this.getBestTime(arena, vehicle) < bestTime)
				bestTime = this.getBestTime(arena, vehicle);

		if(bestTime == Long.MAX_VALUE)
			return -1;

		return bestTime;
	}

	public Entry<String, Long> getBest(String arena)
	{
		long bestTime = Long.MAX_VALUE;
		String bestVehicle = null;
		if(!getArenaSection().isConfigurationSection(arena + ".besttime"))
			return null;

		for(String vehicle : getArenaSection().getConfigurationSection(arena + ".besttime").getKeys(false))
		{
			if(this.alreadyPlayed(arena, vehicle) && this.getBestTime(arena, vehicle) < bestTime)
			{
				bestTime = this.getBestTime(arena, vehicle);
				bestVehicle = vehicle;
			}
		}

		if(bestTime == Long.MAX_VALUE)
			return null;

		return TextUtil.newEntry(bestVehicle, bestTime);
	}
	
	public String getBestVehicle(String arena)
	{
		return getBest(arena) != null ? getBest(arena).getKey() : null;
	}
	
	public boolean alreadyPlayed(String arena, String vehicle)
	{
		return getArenaSection().isLong(arena + ".besttime." + vehicle);
	}
	
	public long getBestTime(String arena, String vehicle)
	{
		return getArenaSection().getLong(arena + ".besttime." + vehicle);
	}
	
	public void setBestTime(long nano, String arena, String vehicle)
	{
		YamlConfiguration section = getArenaSection();
		section.set(arena + ".besttime." + vehicle, nano);
		setArenaSection(section);
	}
	
	public boolean isPurchased(String product)
	{
		return getPurchaseSection().getBoolean(product);
	}
	
	public void purchase(String product)
	{
		setPurchased(product, true);
	}

	public void setPurchased(String product, boolean purchased)
	{
		YamlConfiguration section = getPurchaseSection();
		section.set(product, purchased);
		setPurchaseSection(section);
	}

	public boolean isAchievementComplete(String achievement)
	{
		return getAchievementSection().getBoolean(achievement);
	}

	public void setAchievementCompleted(String achievement, boolean complete)
	{
		YamlConfiguration section = getAchievementSection();
		section.set(achievement, complete);
		setAchievementSection(section);
	}
	
	public int getTotalCollectedItems()
	{
		int total = 0;
		for(VehicleType type : VehicleType.values())
			total += this.getCollectedItems(type.name());
		return total;
	}
	
	public int getCollectedItems(String vehicle)
	{
		return getVehicleSection().getInt(vehicle + ".items", 0);
	}
	
	public void setCollectedItems(int items, String vehicle)
	{
		YamlConfiguration section = getVehicleSection();
		section.set(vehicle + ".items", items);
		setVehicleSection(section);
	}
	
	public int getTotalPassings()
	{
		int total = 0;
		for(VehicleType type : VehicleType.values())
			total += this.getPassings(type.name());
		return total;
	}
	
	public int getPassings(String vehicle)
	{
		return getVehicleSection().getInt(vehicle + ".passings", 0);
	}
	
	public void setPassings(int passings, String vehicle)
	{
		YamlConfiguration section = getVehicleSection();
		section.set(vehicle + ".passings", passings);
		setVehicleSection(section);
	}
	
	public void finish(int position, Arena arena, Vehicle vehicle, long time, int collectedItems, int passings)
	{
		if(position == 1)
		{
			this.setArenaVictories(this.getArenaVictories(arena.getName()) + 1, arena.getName());
			this.setVehicleVictories(this.getVehicleVictories(vehicle.getType().name()) + 1, vehicle.getType().name());
		}
		this.setArenaGamesPlayed(this.getArenaGamesPlayed(arena.getName()) + 1, arena.getName());
		this.setVehicleGamesPlayed(this.getVehicleGamesPlayed(vehicle.getType().name()) + 1, vehicle.getType().name());
		
		if(!this.alreadyPlayed(arena.getName(), vehicle.getType().name()) || time < this.getBestTime(arena.getName(), vehicle.getType().name()))
			this.setBestTime(time, arena.getName(), vehicle.getType().name());
		
		this.setPassings(this.getPassings(vehicle.getType().name()) + passings, vehicle.getType().name());
		
		this.setCollectedItems(this.getCollectedItems(vehicle.getType().name()) + collectedItems, vehicle.getType().name());
	}

	public double getPercentageWin()
	{
		return Math.round((double)this.getTotalVictories() / (double)this.getTotalGamesPlayed() * 1000D) / 10D;
	}
	
	public VehicleType getMostPlayed()
	{
		VehicleType vehicle = null;
		for(VehicleType type : VehicleType.values())
			if(vehicle == null || this.getVehicleGamesPlayed(type.name()) > this.getVehicleGamesPlayed(vehicle.name()))
				vehicle = type;
		return vehicle;
	}
	
	public Set<String> getPlayedArenas()
	{
		return getArenaSection().getKeys(false);
	}

	public static String timeToString(long time)
	{
		double minutes = Math.round(time / 1_000_000d) / 1000d;
		double seconds = Math.round(((int)minutes % 60 + minutes - (int)minutes) * 1000d) / 1000d;
		int millis = ((int)((seconds - (int)seconds) * 1000));

		return (int)minutes / 60 + ":" + (seconds < 10 ? "0" : "") + (int)seconds + "." + (millis < 100 ? "0" : "") + (millis < 10 ? "0" : "") + millis;
	}

	//
	//score INTEGER
	//arena TEXT
	//vehicle TEXT
	//purchase TEXT
	//achievement TEXT
	//

	public static HashMap<String, String> getTables()
	{
		return TextUtil.map("mobracers->score INTEGER, arena TEXT, vehicle TEXT, purchase TEXT, achievement TEXT");
	}

	public static CourseStats get(Player player)
	{
		return new CourseStats(player, Core.getUserDatasManager().getUserData(player));
	}
}
