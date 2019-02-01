package me.winterguardian.core.portal;

import me.winterguardian.core.world.SerializableLocation;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationDestination extends SerializableDestination
{
	private SerializableLocation location;
	
	public LocationDestination(Location loc)
	{
		this.location = new SerializableLocation(loc);
	}
	
	public LocationDestination(String string)
	{
		this.location = SerializableLocation.fromString(string);
	}
	
	@Override
	public void goTo(Player p)
	{	
		p.teleport(location.getLocation());
	}
	
	@Override
	public boolean cancelEvent()
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + ":" + this.location.toString();
	}
	
	public static LocationDestination fromString(String string)
	{
		try
		{
			return new LocationDestination(string);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}