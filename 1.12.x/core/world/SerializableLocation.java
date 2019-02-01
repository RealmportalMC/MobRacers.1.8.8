package me.winterguardian.core.world;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SerializableLocation
{
	
	private String worldName;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private boolean ignoreYawPitch;
	
	public SerializableLocation(String worldName, double x, double y, double z, float yaw, float pitch)
	{
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.ignoreYawPitch = false;
	}
	
	public SerializableLocation(String worldName, double x, double y, double z)
	{
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.ignoreYawPitch = true;
	}
	
	public SerializableLocation(Location loc)
	{
		this.setLocation(loc);
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(this.worldName);
	}
	
	public void teleport(Player p)
	{
		if(p.getVehicle() != null)
			p.leaveVehicle();
		
		if(!this.ignoreYawPitch())
			p.teleport(this.getLocation());
		else
		{
			Location loc = this.getLocation();
			loc.setYaw(p.getLocation().getYaw());
			loc.setPitch(p.getLocation().getPitch());
			p.teleport(loc);
		}
			
	}
	
	public Location getLocation()
	{
		if(this.getWorld() != null)
			return new Location(this.getWorld(), x, y, z, yaw, pitch);
		return null;
	}
	
	public void setLocation(Location loc)
	{
		setLocation(loc, false);
	}
	
	public void setLocation(Location loc, boolean ignoreYawPitch)
	{
		if(loc != null)
		{
			this.worldName = loc.getWorld().getName();
			this.x = loc.getX();
			this.y = loc.getY();
			this.z = loc.getZ();
			this.yaw = loc.getYaw();
			this.pitch = loc.getPitch();
			this.ignoreYawPitch = ignoreYawPitch;
		}
		else
		{
			this.worldName = null;
		}
	}
	
	
	public String toString()
	{
		if(this.worldName != null)
			return this.worldName + "," + this.x + "," + this.y + "," + this.z + "," + (this.ignoreYawPitch() ? "X" : this.yaw) + "," + (this.ignoreYawPitch() ? "X" : this.pitch);
		else
			return "";
	}
	
	public boolean ignoreYawPitch()
	{
		return ignoreYawPitch;
	}
	
	public static SerializableLocation fromString(String s)
	{
		try
		{
			String[] loc = s.split(",");
			if(loc[4].equalsIgnoreCase("X") || loc[5].equalsIgnoreCase("X"))
				return new SerializableLocation(loc[0], Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]));
			return new SerializableLocation(loc[0], Double.parseDouble(loc[1]), Double.parseDouble(loc[2]), Double.parseDouble(loc[3]), Float.parseFloat(loc[4]), Float.parseFloat(loc[5]));
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
