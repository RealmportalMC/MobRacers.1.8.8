package me.winterguardian.mobracers.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import me.winterguardian.core.sorting.AntiRecursiveRandomSelector;
import me.winterguardian.core.sorting.OrderedSelector;
import me.winterguardian.core.sorting.RandomSelector;
import me.winterguardian.core.sorting.Selector;
import me.winterguardian.core.world.SerializableLocation;
import me.winterguardian.core.world.SerializableRegion;
import me.winterguardian.mobracers.MobRacersPlugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Arena
{
	private String name, author;
	private int time;
	private boolean timeLocked, raining;
	
	private SerializableRegion region;
	private List<SerializableLocation> spawnPoints;
	private List<SerializableLocation> items;
	private HashMap<RoadType, Double> roadSpeedModifier;
	private LinkedList<Breakline> lines;
	private int laps, raceTimeLimit, itemRegenDelay;
	
	private List<SerializableLocation> jukeboxes;
	private List<SerializableLocation> spectatorLocations;
	
	private List<SerializableRegion> deathZones;
	
	private Selector<SerializableLocation> spawnPointSelector;

	private ItemStack icon;
	
	public Arena(String name)
	{
		this.name = name;
		this.author = null;
		this.time = 6000;
		this.timeLocked = false;
		this.raining = false;
		
		this.region = null;
		this.spawnPoints = new ArrayList<>();
		this.items = new ArrayList<>();
		this.roadSpeedModifier = new HashMap<>();
		this.lines = new LinkedList<>();
		this.laps = 3;
		this.raceTimeLimit = 300;
		this.itemRegenDelay = 400;
		
		this.jukeboxes = new ArrayList<>();
		this.spectatorLocations = new ArrayList<>();
		
		this.deathZones = new ArrayList<>();
		
		this.spawnPointSelector = new OrderedSelector<>(this.spawnPoints);

		this.icon = new ItemStack(Material.RAILS, 1);
		ItemMeta iconMeta = this.icon.getItemMeta();
		iconMeta.setDisplayName("§a§l" + this.getName());
		this.icon.setItemMeta(iconMeta);
	}
	
	public boolean isReady()
	{
		return this.isRegionReady() && this.spawnPoints.size() > 0 && this.lines.size() >= 2 && this.spectatorLocations.size() > 0;
	}
	
	public boolean isRegionReady()
	{
		if(this.region == null)
			return false;
		
		for(SerializableLocation loc : this.spawnPoints)
			if(!this.region.contains(loc.getLocation()))
				return false;
		
		for(SerializableLocation loc : this.spectatorLocations)
			if(!this.region.contains(loc.getLocation()))
				return false;
		
		for(Breakline line : this.lines)
			if(!this.region.contains(line.getCenter()))
				return false;
		
		return true;
	}
	
	public File getFile()
	{
		return new File(getDirectory(), name + ".yml");
	}
	
	public Location getSpawn()
	{
		return this.spawnPointSelector.next().getLocation();
	}
	
	public void save()
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(this.getFile());
		
		config.set("author", this.author);
		config.set("time", this.time);
		config.set("time-lock", this.timeLocked);
		config.set("rain", this.raining);
		
		config.set("random-spawns", this.spawnPointSelector instanceof RandomSelector);
		config.set("item-regen-delay", this.itemRegenDelay);
		
		config.set("laps", this.laps);
		config.set("race-time-limit", this.raceTimeLimit);
		
		
		if(this.region != null)
			config.set("region", this.region.toString());
		else
			config.set("region", null);
		
		config.set("spawn-points", null);
		int i = 0;
		for(SerializableLocation loc : this.spawnPoints)
			config.set("spawn-points." + i++, loc.toString());
		
		config.set("jukeboxes", null);
		i = 0;
			for(SerializableLocation loc : this.jukeboxes)
				config.set("jukeboxes." + i++, loc.toString());
			
		config.set("spectators", null);
		i = 0;
			for(SerializableLocation loc : this.spectatorLocations)
				config.set("spectators." + i++, loc.toString());
		
		config.set("items", null);
		i = 0;
		for(SerializableLocation loc : this.items)
			config.set("items." + i++, loc.toString());
		
		config.set("material-speed-modifier", null);
		for(RoadType road : this.roadSpeedModifier.keySet())
			if(this.roadSpeedModifier.get(road) != 0)
				config.set("material-speed-modifier." + road.toString(), this.roadSpeedModifier.get(road));
			else
				config.set("material-speed-modifier." + road.toString(), null);
		
		config.set("breaklines", null);
		i = 0;
		for(Breakline line : this.lines)
			config.set("breaklines." + i++, line.toString());
		
		config.set("deathzones", null);
		i = 0;
		for(SerializableRegion region : this.deathZones)
			config.set("deathzones." + i++, region.toString());

		config.set("icon", this.icon);
		
		try
		{
			config.save(this.getFile());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void load()
	{
		YamlConfiguration config = YamlConfiguration.loadConfiguration(this.getFile());
		
		this.author = config.getString("author");
		this.time = config.getInt("time");
		this.timeLocked = config.getBoolean("time-lock");
		this.raining = config.getBoolean("rain");
		
			
		this.itemRegenDelay = config.getInt("item-regen-delay");
		
		
		this.region = SerializableRegion.fromString(config.getString("region"));
		
		if(config.isConfigurationSection("spawn-points"))
			for(String s : config.getConfigurationSection("spawn-points").getKeys(false))
				this.spawnPoints.add(SerializableLocation.fromString(config.getString("spawn-points." + s)));
		
		if(config.isConfigurationSection("jukeboxes"))
			for(String s : config.getConfigurationSection("jukeboxes").getKeys(false))
				this.jukeboxes.add(SerializableLocation.fromString(config.getString("jukeboxes." + s)));
		
		if(config.isConfigurationSection("spectators"))
			for(String s : config.getConfigurationSection("spectators").getKeys(false))
				this.spectatorLocations.add(SerializableLocation.fromString(config.getString("spectators." + s)));
		
		if(config.isConfigurationSection("items"))
			for(String s : config.getConfigurationSection("items").getKeys(false))
				this.items.add(SerializableLocation.fromString(config.getString("items." + s)));
			
		if(config.isConfigurationSection("material-speed-modifier"))
			for(String s : config.getConfigurationSection("material-speed-modifier").getKeys(false))
				this.roadSpeedModifier.put(new RoadType(s), config.getDouble("material-speed-modifier." + s));
		
		if(config.isConfigurationSection("breaklines"))
		{
			this.lines.addAll(Arrays.asList(new Breakline[config.getConfigurationSection("breaklines").getKeys(false).size()]));
			for(String s : config.getConfigurationSection("breaklines").getKeys(false))
				this.lines.set(Integer.parseInt(s), (new Breakline(config.getString("breaklines." + s))));
		}
		
		if(config.isBoolean("random-spawns") && config.getBoolean("random-spawns"))
			this.spawnPointSelector = new AntiRecursiveRandomSelector<SerializableLocation>(this.spawnPoints);
		else
			this.spawnPointSelector = new OrderedSelector<SerializableLocation>(this.spawnPoints);
		
		if(config.isConfigurationSection("deathzones"))
			for(String s : config.getConfigurationSection("deathzones").getKeys(false))
				this.deathZones.add(SerializableRegion.fromString(config.getString("deathzones." + s)));
		
		if(config.isInt("laps"))
			this.laps = config.getInt("laps");
		
		if(config.isInt("race-time-limit"))
			this.raceTimeLimit = config.getInt("race-time-limit");

		if(config.isItemStack("icon"))
			this.icon = config.getItemStack("icon");
		
	}
	
	public boolean delete()
	{
		try
		{
			return this.getFile().delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public String getName()
	{
		return name;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public boolean isTimeLocked()
	{
		return timeLocked;
	}

	public void setTimeLocked(boolean timeLocked)
	{
		this.timeLocked = timeLocked;
	}

	public boolean isRaining()
	{
		return raining;
	}

	public void setRaining(boolean raining)
	{
		this.raining = raining;
	}

	public int getTime()
	{
		return time;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public SerializableRegion getRegion()
	{
		return region;
	}

	public void setRegion(SerializableRegion region)
	{
		this.region = region;
	}

	public List<SerializableLocation> getSpawnPoints()
	{
		return spawnPoints;
	}

	public List<SerializableLocation> getItems()
	{
		return items;
	}

	public HashMap<RoadType, Double> getRoadSpeedModifier()
	{
		return roadSpeedModifier;
	}

	public LinkedList<Breakline> getLines()
	{
		return lines;
	}

	public int getLaps()
	{
		return laps;
	}

	public void setLaps(int laps)
	{
		this.laps = laps;
	}
	
	public int getRaceTimeLimit()
	{
		return raceTimeLimit;
	}

	public void setRaceTimeLimit(int raceTimeLimit)
	{
		this.raceTimeLimit = raceTimeLimit;
	}
	
	public List<SerializableLocation> getJukeboxes() {
		return jukeboxes;
	}

	public void setJukeboxes(List<SerializableLocation> jukeboxes) {
		this.jukeboxes = jukeboxes;
	}

	public List<SerializableLocation> getSpectatorLocations() {
		return spectatorLocations;
	}

	public void setSpectatorLocations(List<SerializableLocation> spectatorLocations)
	{
		this.spectatorLocations = spectatorLocations;
	}

	public int getItemRegenDelay() {
		return itemRegenDelay;
	}

	public void setItemRegenDelay(int itemRegenDelay) {
		this.itemRegenDelay = itemRegenDelay;
	}
	
	public List<SerializableRegion> getDeathZones()
	{
		return this.deathZones;
	}

	public boolean isRandomSpawns()
	{
		return this.spawnPointSelector instanceof RandomSelector;
	}

	public void setRandomSpawns(boolean randomSpawns)
	{
		if(randomSpawns)
			this.spawnPointSelector = new AntiRecursiveRandomSelector<>(this.spawnPoints);
		else
			this.spawnPointSelector = new OrderedSelector<>(this.spawnPoints);
	}

	public ItemStack getIcon()
	{
		return icon;
	}

	public void setIcon(ItemStack icon)
	{
		this.icon = icon;
	}

	public boolean exists()
	{
		return this.getFile().exists();
	}

	public static List<Arena> getArenaList()
	{
		List<Arena> arenas = new ArrayList<>();
		
		for(File file : getDirectory().listFiles())
			if(!file.isDirectory())
				if(file.getName().endsWith(".yml"))
				{
					Arena arena = new Arena(file.getName().replace(".yml", ""));
					arena.load();
					arenas.add(arena);
				}
		
		return arenas;
	}
	
	
	public static List<Arena> getReadyArenaList()
	{
		List<Arena> arenas = new ArrayList<>();
		
		for(File file : getDirectory().listFiles())
			if(!file.isDirectory())
				if(file.getName().endsWith(".yml"))
				{
					Arena arena = new Arena(file.getName().replace(".yml", ""));
					arena.load();
					if(arena.isReady())
						arenas.add(arena);
				}
		
		return arenas;
	}
	
	public static File getDirectory()
	{
		File file = new File(MobRacersPlugin.getPlugin().getDataFolder(), "arenas/");
		if(!file.exists())
			file.mkdir();
		return file;
	}
}
