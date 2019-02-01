package me.winterguardian.core.util;

import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Weather
{
	private int time;
	private WeatherType type;
	private boolean timelocked;
	
	public Weather(int time, WeatherType type, boolean timelocked)
	{
		this.time = time;
		this.type = type;
		this.timelocked = timelocked;
	}

	public void apply(Player player)
	{
		player.setPlayerTime(this.getTime(), !this.isTimelocked());
		player.setPlayerWeather(this.getType());
	}
	
	public void apply(World world)
	{
		world.setTime(this.time);
		world.setGameRuleValue("doDaylightCycle", "" + !this.isTimelocked());
		world.setStorm(this.type == WeatherType.DOWNFALL);
	}
	
	public int getTime()
	{
		return time;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public WeatherType getType()
	{
		return type;
	}

	public void setType(WeatherType type)
	{
		this.type = type;
	}

	public boolean isTimelocked()
	{
		return timelocked;
	}

	public void setTimelocked(boolean timelocked)
	{
		this.timelocked = timelocked;
	}

	public String toString()
	{
		return this.getTime() + "," + this.getType().name() + "," + this.isTimelocked();
	}
	
	public static Weather fromString(String string)
	{
		try
		{
			String[] data = string.split(",");
			return new Weather(Integer.parseInt(data[0]), WeatherType.valueOf(data[1]), Boolean.parseBoolean(data[2]));
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static void reset(Player player)
	{
		player.resetPlayerTime();
		player.resetPlayerWeather();
	}
}
