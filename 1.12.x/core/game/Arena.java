package me.winterguardian.core.game;

import java.io.File;

import me.winterguardian.core.util.Weather;
import me.winterguardian.core.world.SerializableRegion;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public abstract class Arena
{
	private String name, displayName, author;
	private Weather weather;
	private SerializableRegion region;
	
	public Arena(String name)
	{
		this.name = name;
		this.displayName = name;
	}
	
	public abstract boolean isReady();
	protected abstract File getArenaDirectory();

	public String getName()
	{
		return name;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}
	
	
	public Weather getWeather()
	{
		return weather;
	}

	public void setWeather(Weather weather)
	{
		this.weather = weather;
	}

	public SerializableRegion getRegion()
	{
		return region;
	}

	public void setRegion(SerializableRegion region)
	{
		this.region = region;
	}

	public void load()
	{
		try
		{	
			this.load(YamlConfiguration.loadConfiguration(this.getFile()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void save()
	{
		try
		{	
			YamlConfiguration config = YamlConfiguration.loadConfiguration(this.getFile());
			
			this.save(config);
			
			config.save(getFile());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	protected void load(YamlConfiguration config)
	{
		if(config.isString("display-name"))
			this.displayName = config.getString("display-name");
		
		if(config.isString("author"))
			this.author = config.getString("author");
		
		if(config.isString("weather"))
			this.weather = Weather.fromString(config.getString("weather"));
		
		if(config.isString("region"))
			this.region = SerializableRegion.fromString(config.getString("region"));
	}
	
	
	protected void save(YamlConfiguration config)
	{
		config.set("display-name", this.displayName);
		config.set("author", this.author);
		if(this.weather != null)
			config.set("weather", this.weather.toString());
		else
			config.set("weather", null);
		
		if(this.region != null)
			config.set("region", this.region.toString());
		else
			config.set("region", null);
		
	}
	
	protected File getFile()
	{
		if(!getArenaDirectory().isDirectory())
			getArenaDirectory().mkdirs();
		
		return new File(getArenaDirectory(), name + ".arena");
	}
	
	public boolean exists()
	{
		return getFile().exists();
	}
	
	public void delete()
	{
		if(!exists())
			return;
		
		try
		{
			this.getFile().delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
