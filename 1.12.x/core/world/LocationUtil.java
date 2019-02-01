package me.winterguardian.core.world;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;


public class LocationUtil
{
	private LocationUtil(){}
	
	public static void slowTpAll(Collection<Player> players, final Location loc, int delay, Plugin plugin)
	{
		int i = 0;
		for(final Player player : players)
		{
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					player.teleport(loc);
				}
			}, i * delay);
			i++;
		}
	}
	
	public static void slowTpAll(final HashMap<Player, Location> teleportations, int delay, Plugin plugin)
	{
		int i = 0;
		for(final Player player : teleportations.keySet())
		{
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
			{
				@Override
				public void run()
				{
					player.teleport(teleportations.get(player));
				}
			}, i * delay);
			i++;
		}
	}
	
	public static double distanceFromCenter(Block block, Location loc)
	{
		return loc.distance(block.getLocation().add(new Vector(0.5, 0.5, 0.5)));
	}
	
	public static boolean isTeleportable(Location loc)
	{
		for(int y = loc.getBlockY(); y >= 0; y--)
			if(loc.getWorld().getBlockAt(loc.getBlockX(), y, loc.getBlockZ()).getType() != Material.AIR)
				return true;
		return false;
	}
	
	public static double getPlayersTotalDistance(Location loc)
	{
		double d = 0;
		if(loc != null)
			for(Player player : loc.getWorld().getPlayers())
			{
				d += player.getLocation().distance(loc);
			}
		return d;
	}
	
	public static BlockFace getReverse(BlockFace face)
	{
		for(BlockFace current : BlockFace.values())
			if(current.getModX() == -face.getModX()
			&& current.getModY() == -face.getModY()
			&& current.getModZ() == -face.getModZ())
				return current;
		return null;
	}

	public static boolean equalsAsBlock(Location loc1, Location loc2)
	{
		if(!loc1.getWorld().equals(loc2.getWorld()))
			return false;

		if(loc1.getBlockX() != loc2.getBlockX())
			return false;

		if(loc1.getBlockY() != loc2.getBlockY())
			return false;

        return loc1.getBlockZ() == loc2.getBlockZ();
    }
}
